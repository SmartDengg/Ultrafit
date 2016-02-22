package com.example.model.bean.request;

import com.example.common.Constants;
import com.example.common.ultrafit.annotation.Argument;
import com.example.common.ultrafit.annotation.GET;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@GET(stringUrl = Constants.MOVIE_DETAIL_URL) public class MovieDetailRequest {

  @Argument(parameter = "movieid") private String movieId;
  @Argument(parameter = "key") private String appKey = Constants.APP_KEY;
  @Argument(parameter = "dtype") private String dtype = "json";

  public MovieDetailRequest(String movieId) {
    this.movieId = movieId;
  }
}
