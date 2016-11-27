package com.smartdengg.presentation.city;

import com.smartdengg.presentation.BasePresenter;
import com.smartdengg.presentation.BaseView;

/**
 * Created by SmartDengg on 2016/11/27.
 */
interface CityContract {

  interface Presenter<T> extends BasePresenter<CityContract.View<T>> {
    void loadData();
  }

  interface View<T> extends BaseView<T> {
  }
}
