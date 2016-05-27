/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartdengg.model.repository.adapter.rxadapter;

import android.support.annotation.NonNull;
import com.smartdengg.model.repository.annotation.MaxConnect;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * fixed by SmartDengg
 */
public final class RxJavaCallAdapterFactory extends CallAdapter.Factory {

    private boolean gotMaxConnect;

    public static RxJavaCallAdapterFactory create() {
        return new RxJavaCallAdapterFactory();
    }

    private RxJavaCallAdapterFactory() {
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        int[] maxConnect = new int[2];

        for (Annotation annotation : annotations) {
            if (!MaxConnect.class.isAssignableFrom(annotation.getClass())) continue;

            if (this.gotMaxConnect) {
                maxConnect[1] = ((MaxConnect) annotation).count();
                throw new IllegalArgumentException(String.format("At most only one @MaxConnect can be declared, There already exist two " +
                        "value '%s' and '%s'", maxConnect[0], maxConnect[1]));
            }
            maxConnect[0] = ((MaxConnect) annotation).count();
            if (maxConnect[0] < 1) throw new IllegalArgumentException("@MaxConnect's value must not be less than 1");

            this.gotMaxConnect = true;
        }

        Class<?> rawType = getRawType(returnType);
        boolean isObservable = "rx.Observable".equals(rawType.getCanonicalName());
        boolean isSingle = "rx.Single".equals(rawType.getCanonicalName());
        boolean isCompletable = "rx.Completable".equals(rawType.getCanonicalName());

        if (!isObservable && !isSingle && !isCompletable) {
            return null;
        }

        if (!isCompletable && !(returnType instanceof ParameterizedType)) {

            String name = isSingle ? "Single" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized" + " as " + name + "<Foo> or " + name +
                    "<? extends Foo>");
        }

        if (isCompletable) {
            return CompletableHelper.createCallAdapter();
        }

        CallAdapter<Observable<?>> callAdapter = RxJavaCallAdapterFactory.this.getCallAdapter(returnType, maxConnect[0]);
        if (isSingle) {
            return SingleHelper.makeSingle(callAdapter);
        } else {
            return callAdapter;
        }
    }

    private CallAdapter<Observable<?>> getCallAdapter(Type returnType, int maxConnect) {

        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);

        return new SimpleCallAdapter(observableType, maxConnect);
    }

    static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {

        private final Type responseType;
        private final int maxConnect;

        SimpleCallAdapter(Type responseType, int maxConnect) {
            this.responseType = responseType;
            this.maxConnect = maxConnect;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {

            return Observable.create(new CallOnSubscribe<>(call))
                             .lift(OperatorMapResponseToBodyOrError.<R>instance())
                             .retryWhen(new Func1<Observable<? extends Throwable>, Observable<Long>>() {
                                 @Override
                                 public Observable<Long> call(Observable<? extends Throwable> errorObservable) {

                                     return errorObservable.zipWith(Observable.range(1, maxConnect), new Func2<Throwable, Integer, InnerThrowable>() {

                                         @Override
                                         public InnerThrowable call(Throwable throwable, Integer i) {

                                             if (throwable instanceof IOException) {
                                                 return new InnerThrowable(throwable, i);
                                             }
                                             return new InnerThrowable(throwable, maxConnect);
                                         }
                                     })
                                                           .concatMap(new Func1<InnerThrowable, Observable<Long>>() {
                                                               @Override
                                                               public Observable<Long> call(InnerThrowable innerThrowable) {

                                                                   Integer retryCount = innerThrowable.getRetryCount();
                                                                   if (retryCount.equals(maxConnect)) {
                                                                       return Observable.error(innerThrowable.getThrowable());
                                                                   }

                                                                   /*use Schedulers#immediate() to keep on same thread */
                                                                   return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS, Schedulers.immediate());
                                                               }
                                                           });
                                 }
                             });
        }
    }

    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {

        private final Call<T> originalCall;

        CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            Call<T> call = originalCall.clone();

            // Wrap the call in a helper which handles both unsubscription and backpressure.
            RequestArbiter<T> requestArbiter = new RequestArbiter<>(call, subscriber);
            subscriber.add(requestArbiter);
            subscriber.setProducer(requestArbiter);
        }
    }

    static final class RequestArbiter<T> extends AtomicBoolean implements Subscription, Producer {

        private final Call<T> call;
        private final Subscriber<? super Response<T>> subscriber;

        private final AtomicBoolean unsubscribed = new AtomicBoolean(false);

        RequestArbiter(Call<T> call, Subscriber<? super Response<T>> subscriber) {
            this.call = call;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (n < 0) throw new IllegalArgumentException("n < 0: " + n);
            if (n == 0) return; // Nothing to do when requesting 0.
            if (!this.compareAndSet(false, true)) return; // Request was already triggered.

            try {
                Response<T> response = call.execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) subscriber.onError(t);
            }
        }

        @Override
        public void unsubscribe() {
            if (this.unsubscribed.compareAndSet(false, true)) call.cancel();
        }

        @Override
        public boolean isUnsubscribed() {
            return unsubscribed.get() && call.isCanceled();
        }
    }

    private static class InnerThrowable {

        private Throwable throwable;
        private Integer retryCount;

        public InnerThrowable(@NonNull Throwable throwable, @NonNull Integer retryCount) {
            this.throwable = throwable;
            this.retryCount = retryCount;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public Integer getRetryCount() {
            return retryCount;
        }
    }
}
