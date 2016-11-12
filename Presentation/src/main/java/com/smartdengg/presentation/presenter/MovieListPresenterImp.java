package com.smartdengg.presentation.presenter;

import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.domain.interactor.MovieListUseCase;
import com.smartdengg.domain.request.MovieIdRequest;
import com.smartdengg.model.SimpleSubscriber;
import com.smartdengg.domain.errors.WebServiceException;
import com.smartdengg.model.service.movie.MovieService;
import com.smartdengg.presentation.views.ViewInterface;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieListPresenterImp implements MovieListPresenter<List<MovieEntity>> {

  private ViewInterface<List<MovieEntity>> viewInterface;
  private UseCase<MovieIdRequest, List<MovieEntity>> listUseCase;

  private MovieListPresenterImp() {
    this.listUseCase = MovieListUseCase.create(MovieService.createdService());
  }

  public static MovieListPresenterImp createdPresenter() {
    return new MovieListPresenterImp();
  }

  @Override public void attachView(ViewInterface<List<MovieEntity>> view) {
    this.viewInterface = view;
  }

  @Override public void loadData(String cityId) {
    this.listUseCase.subscribe(new MovieIdRequest(Integer.parseInt(cityId)), new ListSubscriber());
  }

  @Override public void detachView() {
    this.listUseCase.unsubscribe();
  }

  private void showContent(final List<MovieEntity> movieEntities) {
    this.viewInterface.showData(Observable.just(movieEntities));
  }

  private void showError(String errorMessage) {
    this.viewInterface.showError(errorMessage);
  }

  private final class ListSubscriber extends SimpleSubscriber<List<MovieEntity>> {

    @Override public void onError(Throwable e) {
      super.onError(e);
      if (e instanceof WebServiceException) {
        MovieListPresenterImp.this.showError(e.getMessage());
      } else {
        MovieListPresenterImp.this.showError(null);
      }
    }

    @Override public void onNext(List<MovieEntity> movieEntities) {
      MovieListPresenterImp.this.showContent(movieEntities);
    }
  }
}
