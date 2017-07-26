package com.smartdengg.model;

import android.support.annotation.CallSuper;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.httpservice.lib.errors.HttpException;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by Joker on 2016/3/14.
 */
public class SimpleSubscriber<T> extends Subscriber<T> {

  @Override public void onCompleted() {
  }

  @CallSuper @Override public void onError(Throwable e) {
    e.printStackTrace();
    Logger.t(Constants.OKHTTP_TAG, 0).e(e.toString());
    if (e instanceof HttpException) {
      Response<?> response = ((HttpException) e).response();
      if (response != null) {
        Logger.t(Constants.OKHTTP_TAG, 1).e(((HttpException) e).response().raw().toString());
      }
    }
  }

  @Override public void onNext(T t) {
  }
}
