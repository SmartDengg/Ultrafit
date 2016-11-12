package com.smartdengg.domain.repository;

import android.support.annotation.NonNull;
import com.smartdengg.domain.response.MovieDetailResponse;
import com.smartdengg.domain.response.MovieListResponse;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * 创建时间:  2016/11/10 15:33 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public interface MovieRepository {
  Observable<List<MovieListResponse>> getMoviesResponse(@NonNull String url,
      @NonNull Map<String, String> params);

  Observable<MovieDetailResponse> getMovieDetailResponse(@NonNull String url,
      @NonNull Map<String, String> params);
}
