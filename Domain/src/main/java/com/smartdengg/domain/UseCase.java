package com.smartdengg.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.domain.rxcompat.SchedulersCompat;
import com.smartdengg.ultra.RequestEntity;
import com.smartdengg.ultra.UltraParserFactory;
import java.util.Map;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public abstract class UseCase<R, S> {

    private Subscription subscription = Subscriptions.empty();

    @SuppressWarnings("unchecked")
    public void subscribe(final R requestEntity, Observer<S> useCaseSubscriber) {
        this.subscribe(requestEntity, useCaseSubscriber, null);
    }

    @SuppressWarnings("unchecked")
    public void subscribe(final R requestEntity, Observer<S> useCaseSubscriber, final Action1<RequestEntity> action1) {

        //*Be care of ConnectableObservable*//
        this.subscription = UltraParserFactory.createParser(requestEntity)
                                              .parseRequestEntity()
                                              .as(Observable.class)
                                              .doOnNext(new Action1<RequestEntity>() {
                                                  @Override
                                                  public void call(RequestEntity requestEntity) {
                                                      if (action1 != null) {
                                                          action1.call(requestEntity);
                                                      } else {
                                                          Logger.t(Constants.OKHTTP_TAG, 0)
                                                                .d(requestEntity.toString());
                                                      }
                                                  }
                                              })
                                              .concatMap(new Func1<RequestEntity, Observable<S>>() {
                                                  @Override
                                                  public Observable<S> call(RequestEntity requestEntity) {
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

    @SuppressWarnings("unchecked")
    public void subscribe(final R requestEntity, final Action1<? super S> onSuccess, final Action1<Throwable> onError) {
        this.subscribe(requestEntity, onSuccess, onError, null);
    }

    @SuppressWarnings("unchecked")
    public void subscribe(final R requestEntity, final Action1<? super S> onSuccess, final Action1<Throwable> onError, final Action1<RequestEntity> action1) {

        this.subscription = UltraParserFactory.createParser(requestEntity)
                                              .parseRequestEntity()
                                              .as(Observable.class)
                                              .doOnNext(new Action1<RequestEntity>() {
                                                  @Override
                                                  public void call(RequestEntity requestEntity) {
                                                      if (action1 != null) {
                                                          action1.call(requestEntity);
                                                      } else {
                                                          Logger.t(Constants.OKHTTP_TAG, 0)
                                                                .d(requestEntity.toString());
                                                      }
                                                  }
                                              })
                                              .concatMap(new Func1<RequestEntity, Observable<S>>() {
                                                  @Override
                                                  public Observable<S> call(RequestEntity requestEntity) {
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
                                              .toSingle()
                                              .subscribe(onSuccess, onError);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    @CheckResult
    protected abstract Observable<S> interactor(@NonNull String url, @NonNull Map<String, String> params);
}
