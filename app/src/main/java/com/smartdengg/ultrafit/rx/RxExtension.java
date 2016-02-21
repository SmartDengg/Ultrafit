package com.smartdengg.ultrafit.rx;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Joker on 2015/11/19.
 */
public final class RxExtension {

  private RxExtension() {
    throw new AssertionError("No instances.");
  }

  @CheckResult @NonNull public static Observable<Void> loadMoreEvent(@NonNull RecyclerView recyclerView) {
    return Observable
        .create(new RecyclerViewLoadMore(recyclerView))
        .onBackpressureBuffer()
        .skip(1)
        .filter(SHOULD_LOADING)
        .map(TRANSLATE_VOID);
  }

  private static final Func1<Boolean, Boolean> SHOULD_LOADING = new Func1<Boolean, Boolean>() {
    @Override public Boolean call(Boolean shouldLoading) {
      return shouldLoading;
    }
  };

  private static final Func1<Boolean, Void> TRANSLATE_VOID = new Func1<Boolean, Void>() {
    @Override public Void call(Boolean aBoolean) {
      return null;
    }
  };
}
