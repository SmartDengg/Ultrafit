package com.smartdengg.presentation.movie;

import com.smartdengg.presentation.MVPPresenter;
import com.smartdengg.presentation.MVPView;

/**
 * Created by SmartDengg on 2016/11/27.
 */
interface MovieContract {

  interface Presenter<T> extends MVPPresenter<View<T>> {
    void loadData(String cityId);
  }

  interface View<T> extends MVPView<T> {
  }
}
