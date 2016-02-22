package com.example.domain;

import android.support.annotation.NonNull;
import com.example.model.bean.MovieService;
import com.example.model.bean.entity.MovieEntity;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class ListUseCase<R> extends UseCase<R, List<MovieEntity>> {

  private MovieService movieService;

  private ListUseCase() {
    this.movieService = MovieService.createdService();
  }

  public static <R> ListUseCase<R> createdUseCase() {
    return (ListUseCase<R>) new ListUseCase();
  }

  @Override protected Observable<List<MovieEntity>> interactor(@NonNull String url, @NonNull Map params) {
    return movieService.getMovieEntities(url, params);
  }
}
