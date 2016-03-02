package com.example.model.bean.request;

import com.example.common.Constants;
import com.example.common.ultrafit.annotation.Argument;
import com.example.common.ultrafit.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@HttpGet(stringUrl = Constants.MOVIE_TODAY_URL) public class MovieIdRequest {

  @Argument(parameter = "cityid") private int cityId;
  @Argument(parameter = "key") private String appKey = Constants.APP_KEY;
  @Argument(parameter = "dtype") private String dtype = "json";

  public MovieIdRequest(int cityId) {
    this.cityId = cityId;
  }
}
