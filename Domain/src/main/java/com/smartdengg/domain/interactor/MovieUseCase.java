package com.smartdengg.domain.interactor;

import android.support.annotation.NonNull;
import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.domain.repository.MovieRepository;
import com.smartdengg.domain.request.MovieDetailRequest;
import com.smartdengg.domain.request.MovieIdRequest;
import com.smartdengg.domain.response.MovieDetailResponse;
import com.smartdengg.domain.response.MovieListResponse;
import com.smartdengg.domain.transforms.MovieEntityTransfer;
import com.smartdengg.domain.transforms.MovieListTransfer;
import com.smartdengg.ultra.RequestEntity;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieUseCase extends UseCase<MovieIdRequest, List<MovieEntity>> {

  private MovieRepository movieRepository;

  private MovieUseCase(MovieRepository movieRepository, Executor<List<MovieEntity>> executor) {
    super(executor);
    this.movieRepository = movieRepository;
  }

  public static MovieUseCase create(MovieRepository movieRepository,
      Executor<List<MovieEntity>> executor) {
    return new MovieUseCase(movieRepository, executor);
  }

  @Override protected Observable<List<MovieEntity>> interactor(@NonNull String url,
      @NonNull Map<String, String> params) {

    return this.fetchMovieList(url, params)
        .compose(MovieListTransfer.create())
        .concatMap(new Func1<RequestEntity<MovieDetailRequest>, Observable<MovieDetailResponse>>() {
          @Override public Observable<MovieDetailResponse> call(
              RequestEntity<MovieDetailRequest> requestEntity) {
            return movieRepository.getMovieDetailResponse(requestEntity.getUrl(),
                requestEntity.getParams());
          }
        })
        .compose(MovieEntityTransfer.create());
  }

  private Observable<List<MovieListResponse>> fetchMovieList(@NonNull String url,
      @NonNull Map<String, String> params) {
    return movieRepository.getMoviesResponse(url, params);
  }
}
