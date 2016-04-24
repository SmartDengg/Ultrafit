package com.smartdengg.model.request;

import com.smartdengg.common.Constants;
import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/22.
 */
@HttpGet(stringUrl = Constants.MOVIE_TODAY_URL)
public class MovieIdRequest extends BaseRequest {

  @Argument(parameter = "cityid") private int cityId;

  public MovieIdRequest(int cityId) {
    this.cityId = cityId;
  }
}
