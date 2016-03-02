package com.example.model.bean.request;

import com.example.common.Constants;
import com.example.common.ultrafit.annotation.Argument;
import com.example.common.ultrafit.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/24.
 */
@HttpGet(stringUrl = Constants.CITY_URL) public class CityListRequest {

  @Argument(parameter = "key") private String appKey = Constants.APP_KEY;
  @Argument(parameter = "dtype") private String dtype = "json";
}
