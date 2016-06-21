package com.smartdengg.model.service;

import android.support.annotation.NonNull;
import com.lianjia.httpservice.annotation.LogResult;
import com.lianjia.httpservice.annotation.RetryCount;
import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.model.request.MovieDetailRequest;
import com.smartdengg.model.response.CityListResponse;
import com.smartdengg.model.response.MovieDetailResponse;
import com.smartdengg.model.response.MovieListResponse;
import com.smartdengg.model.response.base.ResponseS;
import com.smartdengg.model.response.base.ResponseX;
import com.smartdengg.model.service.generator.ServiceGenerator;
import com.smartdengg.ultra.RequestEntity;
import com.smartdengg.ultra.UltraParserFactory;
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

    private static final int MAX_CONNECT = 5;

    private final InternalService service;

    private interface InternalService {

        @LogResult(enable = false)
        @RetryCount(count = MAX_CONNECT)
        @GET
        Observable<ResponseS<CityListResponse>> getCityList(@Url String url, @QueryMap Map<String, String> params);

        @RetryCount(count = MAX_CONNECT)
        @GET
        Observable<ResponseS<MovieListResponse>> getMovieList(@Url String url, @QueryMap Map<String, String> params);

        @RetryCount(count = MAX_CONNECT)
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

        //@formatter:on
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
                                        .concatMap(new Func1<MovieListResponse, Observable<RequestEntity>>() {
                                            @Override
                                            public Observable<RequestEntity> call(MovieListResponse movieListResponse) {

                                                return UltraParserFactory.createParser(new MovieDetailRequest(movieListResponse.movieId))
                                                                         .parseRequestEntity()
                                                                         .as(Observable.class);
                                            }
                                        })
                                        .concatMap(new Func1<RequestEntity, Observable<MovieDetailResponse>>() {
                                            @Override
                                            public Observable<MovieDetailResponse> call(RequestEntity requestEntity) {

                                                return service.getMovieDetail(requestEntity.getUrl(), requestEntity.getParamMap())
                                                              .concatMap(new Func1<ResponseX<MovieDetailResponse>, Observable<MovieDetailResponse>>() {
                                                                  @Override
                                                                  public Observable<MovieDetailResponse> call(
                                                                          ResponseX<MovieDetailResponse> responseX) {
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
