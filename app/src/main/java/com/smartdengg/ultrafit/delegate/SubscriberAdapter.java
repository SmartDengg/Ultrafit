package com.smartdengg.ultrafit.delegate;

import com.orhanobut.logger.Logger;
import rx.Subscriber;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class SubscriberAdapter<T> extends Subscriber<T> {
  @Override public void onCompleted() {

  }

  @Override public void onError(Throwable e) {
    Logger.e(e.getMessage());
  }

  @Override public void onNext(T t) {

  }
}
