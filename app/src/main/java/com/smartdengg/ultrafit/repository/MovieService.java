package com.smartdengg.ultrafit.repository;

import com.smartdengg.ultrafit.bean.response.MovieResponse;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public interface MovieService {

  @GET Observable<MovieResponse> getMovieList(@Url String url, @QueryMap Map<String, String> params);

  @GET Observable<ResponseBody> getMovieDetail(@Url String url);
}
