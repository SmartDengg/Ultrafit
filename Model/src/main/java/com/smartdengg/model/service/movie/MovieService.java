package com.smartdengg.model.service.movie;

import android.support.annotation.NonNull;
import com.smartdengg.httpservice.lib.adapter.rxadapter.rxcompat.SchedulersCompat;
import com.smartdengg.httpservice.lib.annotation.RetryCount;
import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.model.injector.generator.ServiceGenerator;
import com.smartdengg.model.response.MovieDetailResponse;
import com.smartdengg.model.response.MovieListResponse;
import com.smartdengg.model.response.base.ResponseS;
import com.smartdengg.model.response.base.ResponseX;
import com.smartdengg.ultra.core.RequestEntity;
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
public class MovieService {

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

  public Observable<List<MovieEntity>> getMovieEntities(@NonNull String url,
      @NonNull Map<String, String> params) {
    return MovieService.this.service.getMovieList(url, params)
        .compose(new MovieListTransfer())
        .concatMap(new Func1<RequestEntity, Observable<ResponseX<MovieDetailResponse>>>() {
          @Override
          public Observable<ResponseX<MovieDetailResponse>> call(RequestEntity requestEntity) {
            return service.getMovieDetail(requestEntity.getUrl(), requestEntity.getParamMap());
          }
        })
        .compose(new MovieEntityTransfer())
        .compose(SchedulersCompat.<List<MovieEntity>>applyExecutorSchedulers());
  }
}
