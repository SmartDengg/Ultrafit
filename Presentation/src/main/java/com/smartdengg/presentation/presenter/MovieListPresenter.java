package com.smartdengg.presentation.presenter;

import com.smartdengg.presentation.views.ListView;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public interface MovieListPresenter<T> extends Presenter<ListView<T>> {

  void loadData(String cityId);
}
