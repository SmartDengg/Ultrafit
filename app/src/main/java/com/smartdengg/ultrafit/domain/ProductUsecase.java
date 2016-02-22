package com.smartdengg.ultrafit.domain;

import com.orhanobut.logger.Logger;
import com.smartdengg.ultrafit.SchedulersCompat;
import com.smartdengg.ultrafit.bean.entity.MovieEntity;
import com.smartdengg.ultrafit.bean.request.MovieIdRequest;
import com.smartdengg.ultrafit.bean.response.MovieResponse;
import com.smartdengg.ultrafit.repository.MovieService;
import com.smartdengg.ultrafit.service.ServiceGenerator;
import com.smartdengg.ultrafit.ultrafit.RequestEntity;
import com.smartdengg.ultrafit.ultrafit.UltraParser;
import java.util.List;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class ProductUsecase {

  private static MovieService movieService;

  static {
    movieService = ServiceGenerator.createService(MovieService.class);
  }

  @SuppressWarnings("unchecked")
  public static Observable<List<MovieEntity>> getMovieList(MovieIdRequest movieIdRequest) {

    final MovieEntity movieEntity = new MovieEntity();

    movieService
        .getMovieDetail(
            "http://v.juhe.cn/movie/movies" + ".today?dtype=json&key=e0b8426d4f107de733aed080e7ee65f8&cityid=2")
        .subscribe(new Subscriber<ResponseBody>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            Logger.e(e.getMessage());
          }

          @Override public void onNext(ResponseBody responseBody) {

          }
        });

    return Observable.just(movieIdRequest).concatMap(new Func1<MovieIdRequest, Observable<MovieResponse>>() {
      @Override public Observable<MovieResponse> call(MovieIdRequest movieIdRequest) {

        RequestEntity requestEntity = UltraParser.createParser(movieIdRequest).parseRequestEntity();
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
