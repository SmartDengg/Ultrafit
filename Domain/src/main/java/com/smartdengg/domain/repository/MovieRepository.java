package com.smartdengg.domain.repository;

import com.smartdengg.domain.response.MovieDetailResponse;
import com.smartdengg.domain.response.MovieListResponse;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import rx.Observable;

/**
 * 创建时间:  2016/11/10 15:33 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public interface MovieRepository {
  Observable<List<MovieListResponse>> getMoviesResponse(@NotNull String url,
      @NotNull Map<String, String> params);

  Observable<MovieDetailResponse> getMovieDetailResponse(@NotNull String url,
      @NotNull Map<String, String> params);
}
