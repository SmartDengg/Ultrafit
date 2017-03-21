package com.smartdengg.presentation.city;

import com.smartdengg.presentation.MVPPresenter;
import com.smartdengg.presentation.MVPView;

/**
 * Created by SmartDengg on 2016/11/27.
 */
interface CityContract {

  interface Presenter<T> extends MVPPresenter<View<T>> {
    void loadData();
  }

  interface View<T> extends MVPView<T> {
  }
}
