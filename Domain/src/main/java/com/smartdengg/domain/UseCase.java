package com.smartdengg.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.smartdengg.ultra.RequestEntity;
import com.smartdengg.ultra.UltraParser;
import java.util.Map;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public abstract class UseCase<Request, Response> {

  private CompositeSubscription subscriptions = new CompositeSubscription();

  private final Executor<Response> transformer;

  protected UseCase(Executor<Response> transformer) {
    this.transformer = transformer;
  }

  public void subscribe(final Request request, Observer<Response> useCaseSubscriber) {
    this.subscribe(request, useCaseSubscriber, null);
  }

  public void subscribe(final Request request, Observer<Response> useCaseSubscriber,
      final Action1<RequestEntity> onNextAction) {

    /*be care of ConnectableObservable!!!*/
    final UltraParser<Request> ultraParser = UltraParser.createWith(request);
    final Subscription subscription =
        ultraParser.parseAsObservable().doOnNext(new Action1<RequestEntity<Request>>() {
          @Override public void call(RequestEntity<Request> requestEntity) {
            if (onNextAction != null) onNextAction.call(requestEntity);
          }
        }).concatMap(new Func1<RequestEntity<Request>, Observable<Response>>() {
          @Override public Observable<Response> call(RequestEntity<Request> requestEntity) {
            return UseCase.this.interactor(requestEntity.getUrl(), requestEntity.getParams());
          }
        }).onBackpressureDrop().compose(transformer.scheduler()).subscribe(useCaseSubscriber);

    this.subscriptions.add(subscription);
  }

  @CheckResult protected abstract Observable<Response> interactor(@NonNull String url,
      @NonNull Map<String, String> params);

  public void unsubscribe() {
    if (!subscriptions.hasSubscriptions()) subscriptions.clear();
  }

  public interface Executor<T> {

    Observable.Transformer<T, T> scheduler();
  }
}
