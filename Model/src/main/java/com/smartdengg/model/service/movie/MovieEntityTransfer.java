package com.smartdengg.model.service.movie;

import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.model.request.MovieDetailRequest;
import com.smartdengg.model.response.MovieDetailResponse;
import com.smartdengg.model.response.MovieListResponse;
import com.smartdengg.model.response.base.ResponseS;
import com.smartdengg.model.response.base.ResponseX;
import com.smartdengg.ultra.core.RequestEntity;
import com.smartdengg.ultra.core.UltraParserFactory;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
public class MovieEntityTransfer
    implements Observable.Transformer<ResponseS<MovieListResponse>, List<MovieEntity>> {

  private final MovieEntity movieEntityInstance = new MovieEntity();

  private MovieService.InternalService service;

  public MovieEntityTransfer(MovieService.InternalService service) {
    this.service = service;
  }

  @Override public Observable<List<MovieEntity>> call(
      Observable<ResponseS<MovieListResponse>> responseSObservable) {

    return responseSObservable.concatMap(
        new Func1<ResponseS<MovieListResponse>, Observable<List<MovieListResponse>>>() {
          @Override
          public Observable<List<MovieListResponse>> call(ResponseS<MovieListResponse> responseS) {
            return responseS.filterWebServiceErrors();
          }
        }).concatMap(new Func1<List<MovieListResponse>, Observable<MovieListResponse>>() {
      @Override
      public Observable<MovieListResponse> call(List<MovieListResponse> movieListResponses) {
        //return Observable.from(movieListResponses);
        return Observable.just(movieListResponses.get(0));
      }
    }).concatMap(new Func1<MovieListResponse, Observable<RequestEntity>>() {
      @Override public Observable<RequestEntity> call(MovieListResponse movieListResponse) {

        return UltraParserFactory.createParser(new MovieDetailRequest(movieListResponse.movieId))
            .parseRequestEntity()
            .as(Observable.class);
      }
    }).concatMap(new Func1<RequestEntity, Observable<MovieDetailResponse>>() {
      @Override public Observable<MovieDetailResponse> call(RequestEntity requestEntity) {

        return service.getMovieDetail(requestEntity.getUrl(), requestEntity.getParamMap())
            .concatMap(
                new Func1<ResponseX<MovieDetailResponse>, Observable<MovieDetailResponse>>() {
                  @Override public Observable<MovieDetailResponse> call(
                      ResponseX<MovieDetailResponse> responseX) {
                    return responseX.filterWebServiceErrors();
                  }
                });
      }
    }).map(new Func1<MovieDetailResponse, MovieEntity>() {
      @Override public MovieEntity call(MovieDetailResponse movieDetailResponse) {

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
    }).toList();
  }
}
