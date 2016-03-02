package com.example.model.bean;

import android.support.annotation.NonNull;
import com.example.common.SchedulersCompat;
import com.example.common.ultrafit.RequestEntity;
import com.example.common.ultrafit.UltraParserFactory;
import com.example.model.bean.entity.CityEntity;
import com.example.model.bean.entity.MovieEntity;
import com.example.model.bean.repository.ServiceGenerator;
import com.example.model.bean.request.MovieDetailRequest;
import com.example.model.bean.response.CityListResponse;
import com.example.model.bean.response.MovieDetailResponse;
import com.example.model.bean.response.MovieListResponse;
import com.orhanobut.logger.Logger;
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

  private final InternalService service;

  private interface InternalService {

    @GET Observable<CityListResponse> getCityList(@Url String url, @QueryMap Map<String, String> params);

    @GET Observable<MovieListResponse> getMovieList(@Url String url, @QueryMap Map<String, String> params);

    @GET Observable<MovieDetailResponse> getMovieDetail(@Url String url, @QueryMap Map<String, String> params);
  }

  private MovieService() {
    service = ServiceGenerator.createService(InternalService.class);
  }

  public static MovieService createdService() {
    return new MovieService();
  }

  public Observable<List<CityEntity>> getCityEntities(@NonNull String url, @NonNull Map<String, String> params) {

    final CityEntity cityEntityInstance = new CityEntity();

    return MovieService.this.service
        .getCityList(url, params)
        .concatMap(new Func1<CityListResponse, Observable<CityListResponse>>() {
          @Override public Observable<CityListResponse> call(CityListResponse cityListResponse) {
            return cityListResponse.filterWebServiceErrors();
          }
        })
        .concatMap(new Func1<CityListResponse, Observable<CityListResponse.Result>>() {
          @Override public Observable<CityListResponse.Result> call(CityListResponse cityListResponse) {
            return Observable.from(cityListResponse.getResultList());
          }
        })
        .map(new Func1<CityListResponse.Result, CityEntity>() {
          @Override public CityEntity call(CityListResponse.Result result) {

            CityEntity clone = cityEntityInstance.newInstance();

            clone.setCityId(result.cityId);
            clone.setCityName(result.cityName);

            return clone;
          }
        })
        .toList()
        .compose(SchedulersCompat.<List<CityEntity>>applyExecutorSchedulers());
  }

  @SuppressWarnings("unchecked")
  public Observable<List<MovieEntity>> getMovieEntities(@NonNull String url, @NonNull Map<String, String> params) {

    final MovieEntity movieEntityInstance = new MovieEntity();

    return MovieService.this.service
        .getMovieList(url, params)
        .concatMap(new Func1<MovieListResponse, Observable<MovieListResponse>>() {
          @Override public Observable<MovieListResponse> call(MovieListResponse movieListResponse) {
            return movieListResponse.filterWebServiceErrors();
          }
        })
        .concatMap(new Func1<MovieListResponse, Observable<MovieListResponse.Result>>() {
          @Override public Observable<MovieListResponse.Result> call(MovieListResponse movieListResponse) {
            //return Observable.from(movieListResponse.getResultList());

            return Observable.just(movieListResponse.getResultList().get(0));
          }
        })
        .concatMap(new Func1<MovieListResponse.Result, Observable<MovieDetailResponse>>() {
          @Override public Observable<MovieDetailResponse> call(MovieListResponse.Result result) {

            RequestEntity requestEntity =
                UltraParserFactory.createParser(new MovieDetailRequest(result.movieId)).parseRequestEntity();
            Logger.d("Request entity!!! \n Type : %s \n" + "URL : %s \n" + "Params : %s \n",//
                     requestEntity.getRestType().name(), requestEntity.getUrl(), requestEntity.getQueryMap());

            return service
                .getMovieDetail(requestEntity.getUrl(), requestEntity.getQueryMap())
                .concatMap(new Func1<MovieDetailResponse, Observable<MovieDetailResponse>>() {
                  @Override public Observable<MovieDetailResponse> call(MovieDetailResponse movieDetailResponse) {
                    return movieDetailResponse.filterWebServiceErrors();
                  }
                });
          }
        })
        .map(new Func1<MovieDetailResponse, MovieEntity>() {
          @Override public MovieEntity call(MovieDetailResponse movieDetailResponse) {

            MovieDetailResponse.MovieDetail movieDetail = movieDetailResponse.getMovieDetail();

            MovieEntity clone = movieEntityInstance.newInstance();

            clone.setMovieThumbUrl(movieDetail.movieThumbUrl);
            clone.setMovieName(movieDetail.movieName);
            clone.setMovieSketch(movieDetail.movieSketch);

            clone.setMovieWriters(movieDetail.movieWriters);
            clone.setMovieDirector(movieDetail.movieDirectors);
            clone.setMovieActor(movieDetail.movieActors);
            clone.setMovieCategory(movieDetail.movieCategory);
            clone.setMovieScore(movieDetail.movieScore);

            clone.setMovieReleaseTime(movieDetail.movieReleaseTime);
            clone.setMovieCountry(movieDetail.movieCountry);

            return clone;
          }
        })
        .toList()
        .compose(SchedulersCompat.<List<MovieEntity>>applyExecutorSchedulers());
  }
}
