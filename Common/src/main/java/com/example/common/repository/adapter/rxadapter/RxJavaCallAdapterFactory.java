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
package com.example.common.repository.adapter.rxadapter;

import android.support.annotation.NonNull;
import com.example.common.Constants;
import com.example.common.errors.RetrofitHttpException;
import com.example.common.repository.adapter.callAdapter.SmartCallAdapter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public final class RxJavaCallAdapterFactory extends CallAdapter.Factory {
  public static RxJavaCallAdapterFactory create() {
    return new RxJavaCallAdapterFactory();
  }

  private RxJavaCallAdapterFactory() {
  }

  @Override public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

    Class<?> rawType = getRawType(returnType);
    boolean isSingle = "rx.Single".equals(rawType.getCanonicalName());
    boolean isCompletable = "rx.Completable".equals(rawType.getCanonicalName());

    if (rawType != Observable.class && !isSingle && !isCompletable) {
      return null;
    }
    if (!(returnType instanceof ParameterizedType)) {

      String name = "Observable";
      if (isSingle) name = "Single";
      if (isCompletable) name = "Completable";
      throw new IllegalStateException(
          name + " return type must be parameterized" + " as " + name + "<Foo> or " + name + "<? extends Foo>");
    }

    CallAdapter<Observable<?>> callAdapter = getCallAdapter(returnType);
    if (isSingle) {
      return SingleHelper.makeSingle(callAdapter);
    } else if (isCompletable) {
      return CompletableHelper.makeCompletable(callAdapter);
    } else {
      return callAdapter;
    }
  }

  private CallAdapter<Observable<?>> getCallAdapter(Type returnType) {

    Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);

    return new SimpleCallAdapter(observableType);
  }

  static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
    private final Call<T> originalCall;

    CallOnSubscribe(Call<T> originalCall) {
      this.originalCall = originalCall;
    }

    @Override public void call(final Subscriber<? super Response<T>> subscriber) {
      final Call<T> call = originalCall.clone();

      subscriber.add(Subscriptions.create(new Action0() {
        @Override public void call() {
          call.cancel();
        }
      }));

      try {
        Response<T> response = call.execute();
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(response);
          subscriber.onCompleted();
        }
      } catch (Throwable t) {
        /*https://github.com/ReactiveX/RxJava/issues/748#issuecomment-32471495*/
        Exceptions.throwIfFatal(t);
        if (!subscriber.isUnsubscribed()) {
          subscriber.onError(t);
        }
      }
    }
  }

  static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {
    private final Type responseType;

    SimpleCallAdapter(Type responseType) {
      this.responseType = responseType;
    }

    @Override public Type responseType() {
      return responseType;
    }

    @Override public <R> Observable<R> adapt(Call<R> call) {

      return Observable.create(new CallOnSubscribe<>(call)).flatMap(new Func1<Response<R>, Observable<R>>() {
        @Override public Observable<R> call(Response<R> response) {

          Integer code = response.code();

          if (response.isSuccess() && code != SmartCallAdapter.CODE_204 && code != SmartCallAdapter.CODE_205) {
            return Observable.just(response.body());
          }
          return Observable.error(new RetrofitHttpException(response));
        }
      }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<Long>>() {
        @Override public Observable<Long> call(Observable<? extends Throwable> errorObservable) {

          return errorObservable
              .zipWith(Observable.range(1, Constants.MAX_CONNECT), new Func2<Throwable, Integer, InnerThrowable>() {
                @Override public InnerThrowable call(Throwable throwable, Integer i) {

                  if (throwable instanceof IOException) {
                    return new InnerThrowable(throwable, i);
                  }
                  return new InnerThrowable(throwable, Constants.MAX_CONNECT);
                }
              })
              .flatMap(new Func1<InnerThrowable, Observable<Long>>() {
                @Override public Observable<Long> call(InnerThrowable innerThrowable) {

                  Integer retryCount = innerThrowable.getRetryCount();
                  if (retryCount.equals(Constants.MAX_CONNECT)) {
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
