package com.smartdengg.presentation;

import rx.Observable;

/**
 * Created by Joker on 2016/2/22.
 */
public interface MVPView<T> {

  void showData(Observable<T> data);

  void showError(String errorMessage);
}
