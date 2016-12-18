package com.smartdengg.ultra.core;

import android.util.Log;
import com.smartdengg.ultra.annotation.RestType;
import java.util.Map;
import rx.Observable;
import rx.Single;

public class RequestEntity<R> {

  /** Log tag, apps may override it. */
  public static String TAG = RequestEntity.class.getSimpleName();

  private RestType restType;
  private String url;
  private Map<String, String> paramMap;

  private R sourceRequest;

  private boolean shouldOutputs;

  RequestEntity() {
  }

  public RestType getRestType() {
    return restType;
  }

  RequestEntity setRestType(RestType restType) {
    this.restType = restType;
    return RequestEntity.this;
  }

  public String getUrl() {
    return url;
  }

  RequestEntity setUrl(String url) {
    this.url = url;
    return RequestEntity.this;
  }

  public Map<String, String> getParamMap() {
    return paramMap;
  }

  RequestEntity setParamMap(Map<String, String> paramMap) {
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

  //@formatter:off
  void dump() {
    StringBuilder info = new StringBuilder();
    info.append("Type").append('=').append(this.getRestType()).append(Printer.SEPARATOR);
    info.append("Url").append('=').append('\'').append(this.getUrl()).append('\'').append(Printer.SEPARATOR);
    info.append("Params").append('=').append(this.getParamMap()).append(Printer.SEPARATOR);
    info.append("Source").append('=').append(this.sourceRequest).append(Printer.SEPARATOR);
    String result = Printer.translate("Request entity !!!!", info.toString());
    Log.v(TAG, result);
  }

  @Override public String toString() {
    return "RequestEntity{" +
        "restType=" + restType +
        ", url='" + url + '\'' +
        ", paramMap=" + paramMap +
        ", sourceRequest=" + sourceRequest +
        ", shouldOutputs=" + shouldOutputs +
        '}';
  }




}
