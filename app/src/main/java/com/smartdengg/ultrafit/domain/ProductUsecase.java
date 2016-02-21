package com.smartdengg.ultrafit.domain;

import com.orhanobut.logger.Logger;
import com.smartdengg.ultrafit.SchedulersCompat;
import com.smartdengg.ultrafit.bean.entity.MovieEntity;
import com.smartdengg.ultrafit.bean.request.MovieRequest;
import com.smartdengg.ultrafit.bean.response.MovieResponse;
import com.smartdengg.ultrafit.repository.MovieService;
import com.smartdengg.ultrafit.service.ServiceGenerator;
import com.smartdengg.ultrafit.ultrafit.RequestEntity;
import com.smartdengg.ultrafit.ultrafit.UltraParser;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class ProductUsecase {

  private static MovieService movieService;

  static {
    movieService = ServiceGenerator.createService(MovieService.class);
  }

  @SuppressWarnings("unchecked") public static Observable<List<MovieEntity>> getMovieList(MovieRequest movieRequest) {

    final MovieEntity movieEntity = new MovieEntity();

    return Observable.just(movieRequest).concatMap(new Func1<MovieRequest, Observable<MovieResponse>>() {
      @Override public Observable<MovieResponse> call(MovieRequest movieRequest) {

        RequestEntity requestEntity = UltraParser.createParser(movieRequest).parseRequestEntity();
        Logger.d("Begin Request : %s \n" + "URL : %s \n" + "Params : %s \n", requestEntity.getRestType().name(),
                 requestEntity.getUrl(), requestEntity.getQueryMap());

        return movieService
            .getMovieList(requestEntity.getUrl(), requestEntity.getQueryMap())
            .flatMap(new Func1<MovieResponse, Observable<MovieResponse>>() {
              @Override public Observable<MovieResponse> call(MovieResponse productResponse) {

                /** whether the code == 200*/
                return productResponse.filterWebServiceErrors();
              }
            });
      }
    }).concatMap(new Func1<MovieResponse, Observable<MovieResponse.Data.Movie>>() {
      @Override public Observable<MovieResponse.Data.Movie> call(MovieResponse movieResponse) {
        return Observable.from(movieResponse.getData().getMovieList());
      }
    }).map(new Func1<MovieResponse.Data.Movie, MovieEntity>() {
      @Override public MovieEntity call(MovieResponse.Data.Movie movie) {

        MovieEntity clone = movieEntity.newInstance();

        clone.setMovieThumbUrl(movie.movieThumbUrl);

        clone.setMovieName(movie.movieName);
        clone.setMovieDescription(movie.movieDescription);
        clone.setMovieCategory(movie.movieCategory);

        clone.setMovieDirector(movie.movieDirector);
        clone.setMovieActor(movie.movieActor);

        return clone;
      }
    }).toList().compose(SchedulersCompat.<List<MovieEntity>>applyExecutorSchedulers());
  }
}
