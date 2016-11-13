package com.smartdengg.domain.interactor;

import com.smartdengg.domain.UseCase;
import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.domain.repository.MovieRepository;
import com.smartdengg.domain.request.MovieDetailRequest;
import com.smartdengg.domain.request.MovieIdRequest;
import com.smartdengg.domain.response.MovieDetailResponse;
import com.smartdengg.domain.response.MovieListResponse;
import com.smartdengg.domain.transforms.MovieEntityTransfer;
import com.smartdengg.domain.transforms.MovieListTransfer;
import com.smartdengg.ultra.core.RequestEntity;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieListUseCase extends UseCase<MovieIdRequest, List<MovieEntity>> {

  private MovieRepository mMovieRepository;

  private MovieListUseCase(MovieRepository movieRepository,
      Observable.Transformer<List<MovieEntity>, List<MovieEntity>> transformer) {
    super(transformer);
    this.mMovieRepository = movieRepository;
  }

  public static MovieListUseCase create(MovieRepository movieRepository,
      Observable.Transformer<List<MovieEntity>, List<MovieEntity>> transformer) {
    return new MovieListUseCase(movieRepository, transformer);
  }

  @Override protected Observable<List<MovieEntity>> interactor(@NotNull String url,
      @NotNull Map<String, String> params) {

    return this.fetchMovieList(url, params)
        .compose(MovieListTransfer.create())
        .concatMap(new Func1<RequestEntity<MovieDetailRequest>, Observable<MovieDetailResponse>>() {
          @Override public Observable<MovieDetailResponse> call(
              RequestEntity<MovieDetailRequest> requestEntity) {
            return mMovieRepository.getMovieDetailResponse(requestEntity.getUrl(),
                requestEntity.getParamMap());
          }
        })
        .compose(MovieEntityTransfer.create());
  }

  private Observable<List<MovieListResponse>> fetchMovieList(@NotNull String url,
      @NotNull Map<String, String> params) {
    return mMovieRepository.getMoviesResponse(url, params);
  }
}
