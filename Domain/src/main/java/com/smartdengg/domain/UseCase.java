package com.smartdengg.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.ultra.core.RequestEntity;
import com.smartdengg.ultra.core.UltraParserFactory;
import java.util.Map;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public abstract class UseCase<R, S> {

  private Subscription subscription = Subscriptions.empty();

  public void subscribe(final R request, Observer<S> useCaseSubscriber) {
    this.subscribe(request, useCaseSubscriber, null);
  }

  @SuppressWarnings("unchecked")
  public void subscribe(final R request, Observer<S> useCaseSubscriber,
      final Action1<RequestEntity> action) {

    /**be care of ConnectableObservable!!!*/
    this.subscription = Observable.defer(new Func0<Observable<RequestEntity<R>>>() {
      @Override public Observable<RequestEntity<R>> call() {

        return (Observable<RequestEntity<R>>) UltraParserFactory.createParser(request)
            .parseRequestEntity()
            .as(Observable.class);
      }
    }).doOnNext(new Action1<RequestEntity<R>>() {
      @Override public void call(RequestEntity<R> requestEntity) {
        if (action != null) {
          action.call(requestEntity);
        } else {
          Logger.t(Constants.OKHTTP_TAG, 0).d(requestEntity.toString());
        }
      }
    }).concatMap(new Func1<RequestEntity<R>, Observable<S>>() {
      @Override public Observable<S> call(RequestEntity<R> requestEntity) {
        return UseCase.this.interactor(requestEntity.getUrl(), requestEntity.getParamMap());
      }
    }).onBackpressureBuffer().limit(1).subscribe(useCaseSubscriber);
  }

  @SuppressWarnings("unchecked")
  public void subscribe(final R request, final Action1<? super S> onSuccess,
      final Action1<Throwable> onError) {
    this.subscribe(request, onSuccess, onError, null);
  }

  @SuppressWarnings("unchecked")
  public void subscribe(final R request, final Action1<? super S> onSuccess,
      final Action1<Throwable> onError, final Action1<RequestEntity> action) {

    /**you can also use the operator {@link rx.Observable.toSingle}, it's simpler*/
    this.subscription = Single.defer(new Func0<Single<RequestEntity<R>>>() {
      @Override public Single<RequestEntity<R>> call() {
        return (Single<RequestEntity<R>>) UltraParserFactory.createParser(request)
            .parseRequestEntity()
            .as(Single.class);
      }
    }).map(new Func1<RequestEntity<R>, RequestEntity<R>>() {
      @Override public RequestEntity<R> call(RequestEntity<R> requestEntity) {
        if (action != null) {
          action.call(requestEntity);
        } else {
          Logger.t(Constants.OKHTTP_TAG, 0).d(requestEntity.toString());
        }
        return null;
      }
    }).flatMap(new Func1<RequestEntity<R>, Single<S>>() {
      @Override public Single<S> call(RequestEntity<R> requestEntity) {
        return UseCase.this.interactorSingle(requestEntity.getUrl(), requestEntity.getParamMap());
      }
    }).subscribe(onSuccess, onError);
  }

  public void unsubscribe() {
    if (!subscription.isUnsubscribed()) subscription.unsubscribe();
  }

  @CheckResult
  protected Single<S> interactorSingle(@NonNull String url, @NonNull Map<String, String> params) {
    throw new IllegalArgumentException("If you use the 'Single', you must implement this method");
  }

  @CheckResult protected abstract Observable<S> interactor(@NonNull String url,
      @NonNull Map<String, String> params);
}
