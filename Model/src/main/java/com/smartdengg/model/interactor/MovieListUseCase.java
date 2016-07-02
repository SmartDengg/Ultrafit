package com.smartdengg.model.interactor;

import android.support.annotation.NonNull;
import com.smartdengg.domain.UseCase;
import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.model.request.MovieIdRequest;
import com.smartdengg.model.service.movie.MovieService;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieListUseCase extends UseCase<MovieIdRequest, List<MovieEntity>> {

  private MovieService movieService;

  private MovieListUseCase() {
    this.movieService = MovieService.createdService();
  }

  public static MovieListUseCase createdUseCase() {
    return new MovieListUseCase();
  }

  @Override protected Observable<List<MovieEntity>> interactor(@NonNull String url,
      @NonNull Map<String, String> params) {
    return this.movieService.getMovieEntities(url, params);
  }
}
