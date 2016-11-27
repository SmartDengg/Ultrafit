package com.smartdengg.presentation;

/**
 * Created by Joker on 2015/2/22.
 */
public interface BasePresenter<P> {

  void attachView(P view);

  void detachView();
}
