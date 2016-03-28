package com.example.model.bean.request;

import com.example.common.Constants;
import com.example.common.ultrafit.annotation.Argument;

/**
 * Created by Joker on 2016/3/28.
 */
public class BaseRequest {
  @Argument(parameter = "key") private String appKey = Constants.APP_KEY;
  @Argument(parameter = "dtype") private String dtype = "json";
}
