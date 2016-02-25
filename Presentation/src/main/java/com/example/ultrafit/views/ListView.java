package com.example.ultrafit.views;

import java.util.List;
import rx.Observable;

/**
 * Created by Joker on 2016/2/22.
 */
public interface ListView<T> {

  void showDataList(Observable<List<T>> data);

  void showError(String errorMessage);
}
