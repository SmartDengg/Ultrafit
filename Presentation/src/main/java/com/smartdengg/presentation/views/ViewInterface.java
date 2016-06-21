package com.smartdengg.presentation.views;

import rx.Observable;

/**
 * Created by Joker on 2016/2/22.
 */
public interface ViewInterface<T> {

  void showData(Observable<T> data);

  void showError(String errorMessage);
}
