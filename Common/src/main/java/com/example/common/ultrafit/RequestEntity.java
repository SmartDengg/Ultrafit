package com.example.common.ultrafit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.common.ultrafit.type.RestType;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class RequestEntity {

  private RestType restType;
  private String url;
  private Map<String, String> paramMap;

  public RequestEntity() {
  }

  public RequestEntity(@Nullable RestType restType, @Nullable String url, @Nullable Map<String, String> paramsMap) {
    this.restType = restType;
    this.url = url;
    this.paramMap = paramsMap;
  }

  public RestType getRestType() {
    return restType;
  }

  public RequestEntity setRestType(@NonNull RestType restType) {
    this.restType = restType;
    return RequestEntity.this;
  }

  public String getUrl() {
    return url;
  }

  public RequestEntity setUrl(@NonNull String url) {
    this.url = url;
    return RequestEntity.this;
  }

  public Map<String, String> getParamMap() {
    return paramMap;
  }

  public RequestEntity setParamMap(@Nullable Map<String, String> paramMap) {
    this.paramMap = paramMap;
    return RequestEntity.this;
  }

  @Override
  public String toString() {
    return "RequestEntity{" +
        "restType=" + restType +
        ", url='" + url + '\'' +
        ", paramMap=" + paramMap +
        '}';
  }
}
