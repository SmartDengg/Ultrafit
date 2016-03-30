package com.example.common.rxcompat;

import android.support.annotation.CallSuper;
import com.example.common.Constants;
import com.example.common.errors.RetrofitHttpException;
import com.orhanobut.logger.Logger;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by Joker on 2016/3/14.
 */
public class SimpleSubscriber<T> extends Subscriber<T> {

  @Override public void onCompleted() {
  }

  @CallSuper @Override public void onError(Throwable e) {
    Logger.e(e.toString());
    if (e instanceof RetrofitHttpException) {
      Response<?> response = ((RetrofitHttpException) e).response();
      if (response != null)  Logger.t(Constants.OKHTTP_TAG).e(((RetrofitHttpException) e).response().raw().toString());
    }
  }

  @Override public void onNext(T t) {
  }
}
