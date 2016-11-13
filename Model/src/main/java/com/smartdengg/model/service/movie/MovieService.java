package com.smartdengg.model.service.movie;

import android.support.annotation.NonNull;
import com.smartdengg.domain.repository.MovieRepository;
import com.smartdengg.domain.response.MovieDetailResponse;
import com.smartdengg.domain.response.MovieListResponse;
import com.smartdengg.domain.response.base.ResponseS;
import com.smartdengg.domain.response.base.ResponseX;
import com.smartdengg.httpservice.lib.annotation.RetryCount;
import com.smartdengg.model.injector.generator.ServiceGenerator;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieService implements MovieRepository {

  private static final int RETRY_COUNT = 3;

  private final InternalService service;

  interface InternalService {
    @RetryCount(count = RETRY_COUNT) @GET Observable<ResponseS<MovieListResponse>> getMovieList(
        @Url String url, @QueryMap Map<String, String> params);

    @RetryCount(count = RETRY_COUNT) @GET Observable<ResponseX<MovieDetailResponse>> getMovieDetail(
        @Url String url, @QueryMap Map<String, String> params);
  }

  private MovieService() {
    this.service = ServiceGenerator.createService(InternalService.class);
  }

  public static MovieService createdService() {
    return new MovieService();
  }

  @Override public Observable<List<MovieListResponse>> getMoviesResponse(@NonNull String url,
      @NonNull Map<String, String> params) {
    return MovieService.this.service.getMovieList(url, params)
        .concatMap(new Func1<ResponseS<MovieListResponse>, Observable<List<MovieListResponse>>>() {
          @Override
          public Observable<List<MovieListResponse>> call(ResponseS<MovieListResponse> responseS) {
            return responseS.filterWebServiceErrors();
          }
        });
  }

  @Override public Observable<MovieDetailResponse> getMovieDetailResponse(@NonNull String url,
      @NonNull Map<String, String> params) {
    return service.getMovieDetail(url, params)
        .concatMap(new Func1<ResponseX<MovieDetailResponse>, Observable<MovieDetailResponse>>() {
          @Override
          public Observable<MovieDetailResponse> call(ResponseX<MovieDetailResponse> responseX) {
            return responseX.filterWebServiceErrors();
          }
        });
  }
}
