package com.example.ultrafit.presenter;

import com.example.common.Constants;
import com.example.domain.ListUseCase;
import com.example.domain.UseCase;
import com.example.model.bean.entity.MovieEntity;
import com.example.model.bean.request.MovieIdRequest;
import com.example.ultrafit.views.ListView;
import java.util.List;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class ListPresenterImp implements ListPresenter<MovieEntity> {

  private ListView listView;
  private UseCase<MovieIdRequest, List<MovieEntity>> listUseCase;

  public ListPresenterImp() {
    this.listUseCase = ListUseCase.createdUseCase();
  }

  public static ListPresenterImp createdPresenter() {
    return new ListPresenterImp();
  }

  @Override public void attachView(ListView<MovieEntity> view) {
    this.listView = view;
  }

  @Override public void loadData() {
    this.listUseCase.subscribe(new MovieIdRequest(Constants.BEIJING_ID), new ListSubscriber());
  }

  @Override public void detachView() {
    this.listUseCase.unsubscribe();
  }

  @SuppressWarnings("unchecked") private void showContent(List<MovieEntity> movieEntities) {
    this.listView.showMovieList(Observable.just(movieEntities));
  }

  private void showError(String errorMessage) {
    this.listView.showError(errorMessage);
  }

  private final class ListSubscriber extends Subscriber<List<MovieEntity>> {

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
      ListPresenterImp.this.showError(e.getMessage());
    }

    @Override public void onNext(List<MovieEntity> movieEntities) {
      ListPresenterImp.this.showContent(movieEntities);
    }
  }
}
