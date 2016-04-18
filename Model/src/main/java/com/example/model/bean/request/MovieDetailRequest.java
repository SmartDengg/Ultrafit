package com.example.model.bean.request;

import com.example.common.Constants;
import com.smartdengg.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@HttpGet(stringUrl = Constants.MOVIE_DETAIL_URL)
public class MovieDetailRequest {

  @Argument(parameter = "movieid") private String movieId;
  @Argument(parameter = "key") private String appKey = Constants.APP_KEY;
  @Argument(parameter = "dtype") private String dtype = "json";

  public MovieDetailRequest(String movieId) {
    this.movieId = movieId;
  }
}
