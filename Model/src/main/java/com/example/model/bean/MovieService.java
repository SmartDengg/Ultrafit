package com.example.model.bean;

import android.support.annotation.NonNull;
import com.example.common.repository.ServiceGenerator;
import com.example.common.ultrafit.RequestEntity;
import com.example.common.ultrafit.UltraParserFactory;
import com.example.model.bean.entity.CityEntity;
import com.example.model.bean.entity.MovieEntity;
import com.example.model.bean.request.MovieDetailRequest;
import com.example.model.bean.response.CityListResponse;
import com.example.model.bean.response.MovieDetailResponse;
import com.example.model.bean.response.MovieListResponse;
import com.example.model.bean.response.base.ResponseS;
import com.example.model.bean.response.base.ResponseX;
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

        @GET
        Observable<ResponseS<CityListResponse>> getCityList(@Url String url, @QueryMap Map<String, String> params);

        @GET
        Observable<ResponseS<MovieListResponse>> getMovieList(@Url String url, @QueryMap Map<String, String> params);

        @GET
        Observable<ResponseX<MovieDetailResponse>> getMovieDetail(@Url String url, @QueryMap Map<String, String> params);
    }

    private MovieService() {
        service = ServiceGenerator.createService(InternalService.class);
    }

    public static MovieService createdService() {
        return new MovieService();
    }

    public Observable<List<CityEntity>> getCityEntities(@NonNull String url, @NonNull Map<String, String> params) {

        final CityEntity cityEntityInstance = new CityEntity();

        return MovieService.this.service.getCityList(url, params)
                                        .concatMap(new Func1<ResponseS<CityListResponse>, Observable<List<CityListResponse>>>() {
                                            @Override
                                            public Observable<List<CityListResponse>> call(ResponseS<CityListResponse> responseS) {
                                                return responseS.filterWebServiceErrors();
                                            }
                                        })
                                        .concatMap(new Func1<List<CityListResponse>, Observable<CityListResponse>>() {
                                            @Override
                                            public Observable<CityListResponse> call(List<CityListResponse> cityListResponses) {
                                                return Observable.from(cityListResponses);
                                            }
                                        })
                                        .map(new Func1<CityListResponse, CityEntity>() {
                                            @Override
                                            public CityEntity call(CityListResponse cityListResponse) {

                                                CityEntity clone = cityEntityInstance.newInstance();

                                                clone.setCityId(cityListResponse.cityId);
                                                clone.setCityName(cityListResponse.cityName);

                                                return clone;
                                            }
                                        })
                                        .toList();
    }

    @SuppressWarnings("unchecked")
    public Observable<List<MovieEntity>> getMovieEntities(@NonNull String url, @NonNull Map<String, String> params) {

        final MovieEntity movieEntityInstance = new MovieEntity();

        return MovieService.this.service.getMovieList(url, params)
                                        .concatMap(new Func1<ResponseS<MovieListResponse>, Observable<List<MovieListResponse>>>() {
                                            @Override
                                            public Observable<List<MovieListResponse>> call(ResponseS<MovieListResponse> responseS) {
                                                return responseS.filterWebServiceErrors();
                                            }
                                        })
                                        .concatMap(new Func1<List<MovieListResponse>, Observable<MovieListResponse>>() {
                                            @Override
                                            public Observable<MovieListResponse> call(List<MovieListResponse> movieListResponses) {
                                                //return Observable.from(movieListResponses);

                                                return Observable.just(movieListResponses.get(0));
                                            }
                                        })
                                        .concatMap(new Func1<MovieListResponse, Observable<MovieDetailResponse>>() {
                                            @Override
                                            public Observable<MovieDetailResponse> call(MovieListResponse response) {

                                                RequestEntity requestEntity =
                                                        UltraParserFactory.createParser(new MovieDetailRequest(response.movieId))
                                                                          .parseRequestEntity();
                                                UltraParserFactory.outputs(requestEntity);

                                                return service.getMovieDetail(requestEntity.getUrl(), requestEntity.getParamMap())
                                                              .concatMap(new Func1<ResponseX<MovieDetailResponse>, Observable<MovieDetailResponse>>() {
                                                                  @Override
                                                                  public Observable<MovieDetailResponse> call(ResponseX<MovieDetailResponse> responseX) {
                                                                      return responseX.filterWebServiceErrors();
                                                                  }
                                                              });
                                            }
                                        })
                                        .map(new Func1<MovieDetailResponse, MovieEntity>() {
                                            @Override
                                            public MovieEntity call(MovieDetailResponse movieDetailResponse) {

                                                MovieEntity clone = movieEntityInstance.newInstance();

                                                clone.setMovieThumbUrl(movieDetailResponse.movieThumbUrl);
                                                clone.setMovieName(movieDetailResponse.movieName);
                                                clone.setMovieSketch(movieDetailResponse.movieSketch);

                                                clone.setMovieWriters(movieDetailResponse.movieWriters);
                                                clone.setMovieDirector(movieDetailResponse.movieDirectors);
                                                clone.setMovieActor(movieDetailResponse.movieActors);
                                                clone.setMovieCategory(movieDetailResponse.movieCategory);
                                                clone.setMovieScore(movieDetailResponse.movieScore);

                                                clone.setMovieReleaseTime(movieDetailResponse.movieReleaseTime);
                                                clone.setMovieCountry(movieDetailResponse.movieCountry);

                                                return clone;
                                            }
                                        })
                                        .toList();
    }
}
