package com.smartdengg.model.service.movie;

import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.model.response.MovieDetailResponse;
import com.smartdengg.model.response.base.ResponseX;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
public class MovieEntityTransfer
    implements Observable.Transformer<ResponseX<MovieDetailResponse>, List<MovieEntity>> {

  private final MovieEntity movieEntityInstance = new MovieEntity();

  @Override public Observable<List<MovieEntity>> call(
      Observable<ResponseX<MovieDetailResponse>> xObservable) {

    return xObservable.concatMap(
        new Func1<ResponseX<MovieDetailResponse>, Observable<MovieDetailResponse>>() {
          @Override
          public Observable<MovieDetailResponse> call(ResponseX<MovieDetailResponse> responseX) {
            return responseX.filterWebServiceErrors();
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
