package com.smartdengg.model;

import android.support.annotation.CallSuper;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.model.errors.RetrofitHttpException;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by Joker on 2016/3/14.
 */
public class SimpleSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        Logger.t(Constants.OKHTTP_TAG, 0).e(e.toString());
        if (e instanceof RetrofitHttpException) {
            Response<?> response = ((RetrofitHttpException) e).response();
            if (response != null) Logger.t(Constants.OKHTTP_TAG, 1).e(((RetrofitHttpException) e).response().raw().toString());
        }
    }

    @Override
    public void onNext(T t) {
    }
}
