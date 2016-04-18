package com.example.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.example.common.rxcompat.SchedulersCompat;
import com.example.common.ultrafit.RequestEntity;
import com.example.common.ultrafit.UltraParserFactory;
import java.util.Map;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public abstract class UseCase<R, S> {

    private Subscription subscription = Subscriptions.empty();

    //@formatter:off
    @SuppressWarnings("unchecked")
    public void subscribe(final R requestEntity, Observer<S> useCaseSubscriber) {

        /*Be care of ConnectableObservable*/
        this.subscription = Observable.fromCallable(new Func0<R>() {
                                          @Override
                                          public R call() {
                                              return requestEntity;
                                          }
                                      })
                                      .concatMap(new Func1<R, Observable<S>>() {
                                          @Override
                                          public Observable<S> call(R r) {

                                              RequestEntity requestEntity = UltraParserFactory.createParser(r)
                                                                                              .parseRequestEntity();

                                              return UseCase.this.interactor(requestEntity.getUrl(), requestEntity.getParamMap());
                                          }
                                      })
                                      .onBackpressureBuffer()
                                      .takeFirst(new Func1<S, Boolean>() {
                                          @Override
                                          public Boolean call(S s) {
                                              return !subscription.isUnsubscribed();
                                          }
                                      })
                                      .compose(SchedulersCompat.<S>applyExecutorSchedulers())
                                      .subscribe(useCaseSubscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    @CheckResult
    protected abstract Observable<S> interactor(@NonNull String url, @NonNull Map<String, String> params);
}
