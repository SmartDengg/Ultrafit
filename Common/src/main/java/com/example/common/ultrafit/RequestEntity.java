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

  private Map<String, String> queryMap;

  public RequestEntity() {
  }

  public RequestEntity(@Nullable RestType restType, @Nullable String url, @Nullable Map<String, String> queryMap) {
    this.restType = restType;
    this.url = url;
    this.queryMap = queryMap;
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

  public Map<String, String> getQueryMap() {
    return queryMap;
  }

  public RequestEntity setQueryMap(@Nullable Map<String, String> queryMap) {
    this.queryMap = queryMap;
    return RequestEntity.this;
  }

  @Override public String toString() {
    return "RequestEntity{" +
        "restType=" + restType +
        ", url='" + url + '\'' +
        ", queryMap=" + queryMap +
        '}';
  }
}
