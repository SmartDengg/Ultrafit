package com.example.model.bean.request;

import com.example.common.Constants;
import com.smartdengg.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.annotation.HttpGet;

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
