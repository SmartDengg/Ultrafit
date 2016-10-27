package com.smartdengg.model.service.movie;

import com.smartdengg.model.request.MovieDetailRequest;
import com.smartdengg.model.response.MovieListResponse;
import com.smartdengg.model.response.base.ResponseS;
import com.smartdengg.ultra.core.RequestEntity;
import com.smartdengg.ultra.core.UltraParserFactory;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/7/2.
 */
@SuppressWarnings("unchecked") public class MovieListTransfer implements
    Observable.Transformer<ResponseS<MovieListResponse>, RequestEntity<MovieDetailRequest>> {

  @Override public Observable<RequestEntity<MovieDetailRequest>> call(
      Observable<ResponseS<MovieListResponse>> sObservable) {

    return sObservable.concatMap(
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
    }).concatMap(new Func1<MovieListResponse, Observable<RequestEntity<MovieDetailRequest>>>() {
      @Override public Observable<RequestEntity<MovieDetailRequest>> call(
          MovieListResponse movieListResponse) {

        MovieDetailRequest movieDetailRequest = new MovieDetailRequest(movieListResponse.movieId);

        return (Observable<RequestEntity<MovieDetailRequest>>) UltraParserFactory.createParser(
            movieDetailRequest).parseRequestEntity().asObservable();
      }
    });
  }
}
