package com.smartdengg.presentation.movie;

import com.smartdengg.presentation.BasePresenter;
import com.smartdengg.presentation.BaseView;

/**
 * Created by SmartDengg on 2016/11/27.
 */
interface MovieContract {

  interface Presenter<T> extends BasePresenter<MovieContract.View<T>> {
    void loadData(String cityId);
  }

  interface View<T> extends BaseView<T> {
  }
}
