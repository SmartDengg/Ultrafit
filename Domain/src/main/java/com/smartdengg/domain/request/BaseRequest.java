package com.smartdengg.domain.request;

import com.smartdengg.ultra.annotation.Argument;

/**
 * Created by Joker on 2016/3/28.
 */
public class BaseRequest {
  @Argument(parameter = "key") private String appKey = "e0b8426d4f107de733aed080e7ee65f8";
  @Argument private String dtype = "json";
}
