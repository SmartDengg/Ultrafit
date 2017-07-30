package com.smartdengg.domain.request;

import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.Http;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@Http(url = "movie/query", value = Http.Type.GET) public class MovieDetailRequest
    extends BaseRequest {

  @Argument(parameter = "movieid") private String movieId;

  public MovieDetailRequest(String movieId) {
    this.movieId = movieId;
  }
}
