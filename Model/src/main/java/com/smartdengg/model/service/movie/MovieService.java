package com.smartdengg.model.service.movie;

import android.support.annotation.NonNull;
import com.smartdengg.httpservice.lib.annotation.RetryCount;
import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.model.injector.generator.ServiceGenerator;
import com.smartdengg.model.response.MovieDetailResponse;
import com.smartdengg.model.response.MovieListResponse;
import com.smartdengg.model.response.base.ResponseS;
import com.smartdengg.model.response.base.ResponseX;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieService {

  private static final int MAX_CONNECT = 5;

  private final InternalService service;

  interface InternalService {
    @RetryCount(count = MAX_CONNECT) @GET Observable<ResponseS<MovieListResponse>> getMovieList(
        @Url String url, @QueryMap Map<String, String> params);

    @RetryCount(count = MAX_CONNECT) @GET Observable<ResponseX<MovieDetailResponse>> getMovieDetail(
        @Url String url, @QueryMap Map<String, String> params);
  }

  private MovieService() {
    this.service = ServiceGenerator.createService(InternalService.class);
  }

  public static MovieService createdService() {
    return new MovieService();
  }

  public Observable<List<MovieEntity>> getMovieEntities(@NonNull String url,
      @NonNull Map<String, String> params) {
    return MovieService.this.service.getMovieList(url, params)
        .compose(new MovieEntityTransfer(service));
  }
}
