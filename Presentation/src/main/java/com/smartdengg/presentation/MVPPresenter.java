package com.smartdengg.presentation;

/**
 * Created by Joker on 2015/2/22.
 */
public interface MVPPresenter<V> {

  void attachView(V view);

  void detachView();
}
