package com.smartdengg.presentation.movie;

import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.domain.errors.WebServiceException;
import com.smartdengg.domain.interactor.MovieUseCase;
import com.smartdengg.domain.request.MovieIdRequest;
import com.smartdengg.httpservice.lib.adapter.rxadapter.rxcompat.SchedulersCompat;
import com.smartdengg.model.SimpleSubscriber;
import com.smartdengg.model.service.movie.MovieService;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
class MoviePresenterImp implements MovieContract.Presenter<List<MovieEntity>> {

  private MovieContract.View<List<MovieEntity>> view;
  private UseCase<MovieIdRequest, List<MovieEntity>> movieUseCase;

  private MoviePresenterImp() {
    this.movieUseCase = MovieUseCase.create(MovieService.createdService(),
        SchedulersCompat.<List<MovieEntity>>applyExecutorSchedulers());
  }

  static MoviePresenterImp createdPresenter() {
    return new MoviePresenterImp();
  }

  @Override public void attachView(MovieContract.View<List<MovieEntity>> view) {
    this.view = view;
  }

  @Override public void loadData(String cityId) {
    this.movieUseCase.subscribe(new MovieIdRequest(Integer.parseInt(cityId)), new Subscriber());
  }

  @Override public void detachView() {
    this.movieUseCase.unsubscribe();
  }

  private void showContent(final List<MovieEntity> movieEntities) {
    this.view.showData(Observable.just(movieEntities));
  }

  private void showError(String errorMessage) {
    this.view.showError(errorMessage);
  }

  private final class Subscriber extends SimpleSubscriber<List<MovieEntity>> {

    @Override public void onError(Throwable e) {
      super.onError(e);
      if (e instanceof WebServiceException) {
        MoviePresenterImp.this.showError(e.getMessage());
      } else {
        MoviePresenterImp.this.showError(null);
      }
    }

    @Override public void onNext(List<MovieEntity> movieEntities) {
      MoviePresenterImp.this.showContent(movieEntities);
    }
  }
}
