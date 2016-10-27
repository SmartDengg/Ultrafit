package com.smartdengg.ultra.core;

import com.smartdengg.ultra.annotation.RestType;
import java.util.Map;
import rx.Observable;
import rx.Single;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class RequestEntity<R> {

  private RestType restType;
  private String url;
  private Map<String, String> paramMap;

  private R sourceRequest;

  private boolean shouldOutputs;

  public RequestEntity() {
  }

  public RestType getRestType() {
    return restType;
  }

  public RequestEntity setRestType(RestType restType) {
    this.restType = restType;
    return RequestEntity.this;
  }

  public String getUrl() {
    return url;
  }

  public RequestEntity setUrl(String url) {
    this.url = url;
    return RequestEntity.this;
  }

  public Map<String, String> getParamMap() {
    return paramMap;
  }

  public RequestEntity setParamMap(Map<String, String> paramMap) {
    this.paramMap = paramMap;
    return RequestEntity.this;
  }

  public R getSourceRequest() {
    return sourceRequest;
  }

  public void setSourceRequest(R sourceEntity) {
    this.sourceRequest = sourceEntity;
  }

  public boolean isShouldOutputs() {
    return shouldOutputs;
  }

  public RequestEntity setShouldOutputs(boolean shouldOutputs) {
    this.shouldOutputs = shouldOutputs;
    return RequestEntity.this;
  }

  public Observable<RequestEntity<R>> asObservable() {
    return Observable.just(RequestEntity.this);
  }

  public Single<RequestEntity<R>> asSingle() {
    return Single.just(RequestEntity.this);
  }

  @Override public String toString() {
    return "Request entity !!!!" +
        "\n  -->  Type    : " + RequestEntity.this.getRestType() +
        "\n  -->  Url     : " + RequestEntity.this.getUrl() +
        "\n  -->  Params  : " + RequestEntity.this.getParamMap();
  }
}
