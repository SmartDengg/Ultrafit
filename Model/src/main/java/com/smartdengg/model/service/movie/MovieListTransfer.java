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
public class MovieListTransfer
    implements Observable.Transformer<ResponseS<MovieListResponse>, RequestEntity> {

  @Override
  public Observable<RequestEntity> call(Observable<ResponseS<MovieListResponse>> sObservable) {

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
    }).concatMap(new Func1<MovieListResponse, Observable<RequestEntity>>() {
      @Override public Observable<RequestEntity> call(MovieListResponse movieListResponse) {

        return UltraParserFactory.createParser(new MovieDetailRequest(movieListResponse.movieId))
            .parseRequestEntity()
            .as(Observable.class);
      }
    });
  }
}