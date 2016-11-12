package com.smartdengg.domain.transforms;

import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.domain.response.MovieDetailResponse;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
public class MovieEntityTransfer
    implements Observable.Transformer<MovieDetailResponse, List<MovieEntity>> {

  private final MovieEntity movieEntityInstance = new MovieEntity();

  public static MovieEntityTransfer create() {
    return new MovieEntityTransfer();
  }

  private MovieEntityTransfer() {
  }

  @Override public Observable<List<MovieEntity>> call(Observable<MovieDetailResponse> observable) {

    return observable.map(new Func1<MovieDetailResponse, MovieEntity>() {
      @Override public MovieEntity call(MovieDetailResponse response) {

        MovieEntity clone = movieEntityInstance.newInstance();

        clone.setMovieThumbUrl(response.poster);
        clone.setMovieName(response.title);
        clone.setMovieSketch(response.movieSketch);

        clone.setMovieWriters(response.writers);
        clone.setMovieDirector(response.directors);
        clone.setMovieActor(response.actors);
        clone.setMovieCategory(response.genres);
        clone.setMovieScore(response.rating);

        clone.setMovieReleaseTime(response.movieReleaseTime);
        clone.setMovieCountry(response.movieCountry);

        return clone;
      }
    }).toList();
  }
}
