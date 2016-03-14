package com.example.common;

import android.support.annotation.CallSuper;
import com.orhanobut.logger.Logger;
import rx.Subscriber;

/**
 * Created by Joker on 2016/3/14.
 */
public class SimpleSubscriber<T> extends Subscriber<T> {

  @Override public void onCompleted() {
  }

  @CallSuper @Override public void onError(Throwable e) {
    Logger.e(e.toString());
  }

  @Override public void onNext(T t) {
  }
}
