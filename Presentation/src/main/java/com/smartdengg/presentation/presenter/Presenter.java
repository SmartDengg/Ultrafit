package com.smartdengg.presentation.presenter;

/**
 * Created by Joker on 2015/2/22.
 */
public interface Presenter<P> {

  void attachView(P view);

  void detachView();
}
