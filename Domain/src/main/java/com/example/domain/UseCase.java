package com.example.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.example.common.Constants;
import com.example.common.rxcompat.SchedulersCompat;
import com.orhanobut.logger.Logger;
import com.smartdengg.ultrafit.RequestEntity;
import com.smartdengg.ultrafit.UltraParserFactory;
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

        //*Be care of ConnectableObservable*//
        this.subscription = UltraParserFactory.createParser(requestEntity)
                                              .parseRequestEntity()
                                              .asObservable()
                                              .doOnNext(new Action1<RequestEntity>() {
                                                  @Override
                                                  public void call(RequestEntity requestEntity) {
                                                      Logger.t(Constants.OKHTTP_TAG, 0)
                                                            .d(requestEntity.toString());
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

        this.subscription = UltraParserFactory.createParser(requestEntity)
                                              .parseRequestEntity()
                                              .asObservable()
                                              .doOnNext(new Action1<RequestEntity>() {
                                                  @Override
                                                  public void call(RequestEntity requestEntity) {
                                                      Logger.t(Constants.OKHTTP_TAG, 0)
                                                            .d(requestEntity.toString());
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
