package com.smartdengg.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.domain.rxcompat.SchedulersCompat;
import com.smartdengg.domain.rxcompat.SmartExecutors;
import com.smartdengg.ultra.core.RequestEntity;
import com.smartdengg.ultra.core.UltraParserFactory;
import java.util.Map;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
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
  public void subscribe(final R requestEntity, Observer<S> useCaseSubscriber,
      final Action1<RequestEntity> action) {

    /**be care of ConnectableObservable!!!*/
    this.subscription = Observable.defer(new Func0<Observable<RequestEntity>>() {
      @Override public Observable<RequestEntity> call() {

        return UltraParserFactory.createParser(requestEntity)
            .parseRequestEntity()
            .as(Observable.class);
      }
    }).doOnNext(new Action1<RequestEntity>() {
      @Override public void call(RequestEntity requestEntity) {
        if (action != null) {
          action.call(requestEntity);
        } else {
          Logger.t(Constants.OKHTTP_TAG, 0).d(requestEntity.toString());
        }
      }
    }).concatMap(new Func1<RequestEntity, Observable<S>>() {
      @Override public Observable<S> call(RequestEntity requestEntity) {
        return UseCase.this.interactor(requestEntity.getUrl(), requestEntity.getParamMap());
      }
    }).onBackpressureBuffer().takeFirst(new Func1<S, Boolean>() {
      @Override public Boolean call(S s) {
        return !subscription.isUnsubscribed();
      }
    }).compose(SchedulersCompat.<S>applyExecutorSchedulers()).subscribe(useCaseSubscriber);
  }

  @SuppressWarnings("unchecked")
  public void subscribe(final R requestEntity, final Action1<? super S> onSuccess,
      final Action1<Throwable> onError) {
    this.subscribe(requestEntity, onSuccess, onError, null);
  }

  @SuppressWarnings("unchecked")
  public void subscribe(final R requestEntity, final Action1<? super S> onSuccess,
      final Action1<Throwable> onError, final Action1<RequestEntity> action) {

    /**you can also use the operator {@link rx.Observable.toSingle}, it's simpler*/
    this.subscription = Single.defer(new Func0<Single<RequestEntity>>() {
      @Override public Single<RequestEntity> call() {
        return UltraParserFactory.createParser(requestEntity).parseRequestEntity().as(Single.class);
      }
    })
        .map(new Func1<RequestEntity, RequestEntity>() {
          @Override public RequestEntity call(RequestEntity requestEntity) {
            if (action != null) {
              action.call(requestEntity);
            } else {
              Logger.t(Constants.OKHTTP_TAG, 0).d(requestEntity.toString());
            }
            return null;
          }
        })
        .flatMap(new Func1<RequestEntity, Single<S>>() {
          @Override public Single<S> call(RequestEntity requestEntity) {
            return UseCase.this.interactorSingle(requestEntity.getUrl(),
                requestEntity.getParamMap());
          }
        })
        .subscribeOn(Schedulers.from(SmartExecutors.eventExecutor))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError);
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
