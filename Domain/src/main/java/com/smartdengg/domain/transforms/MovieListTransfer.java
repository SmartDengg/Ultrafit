package com.smartdengg.domain.transforms;

import com.smartdengg.domain.request.MovieDetailRequest;
import com.smartdengg.domain.response.MovieListResponse;
import com.smartdengg.ultra.RequestEntity;
import com.smartdengg.ultra.UltraParser;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
@SuppressWarnings("unchecked") public class MovieListTransfer
    implements Observable.Transformer<List<MovieListResponse>, RequestEntity<MovieDetailRequest>> {

  private MovieListTransfer() {
  }

  public static MovieListTransfer create() {
    return new MovieListTransfer();
  }

  @Override public Observable<RequestEntity<MovieDetailRequest>> call(
      Observable<List<MovieListResponse>> observable) {

    return observable.concatMap(
        new Func1<List<MovieListResponse>, Observable<MovieListResponse>>() {
          @Override public Observable<MovieListResponse> call(List<MovieListResponse> responses) {
            //return Observable.from(responses);
            return Observable.just(responses.get(0));
          }
        }).concatMap(new Func1<MovieListResponse, Observable<RequestEntity<MovieDetailRequest>>>() {
      @Override public Observable<RequestEntity<MovieDetailRequest>> call(
          MovieListResponse movieListResponse) {
        MovieDetailRequest movieDetailRequest = new MovieDetailRequest(movieListResponse.movieId);
        return UltraParser.createWith(movieDetailRequest).parseAsObservable();
      }
    });
  }
}
