package com.smartdengg.domain.request;

import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.Http;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@Http(url = "movie/movies.today", value = Http.Type.GET) public class MovieIdRequest
    extends BaseRequest {

  @Argument(parameter = "cityid") private int cityId;

  public MovieIdRequest(int cityId) {
    this.cityId = cityId;
  }
}
