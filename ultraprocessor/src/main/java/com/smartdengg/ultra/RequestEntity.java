package com.smartdengg.ultra;

import com.smartdengg.jsonprinter.JsonPrinter;
import com.smartdengg.ultra.annotation.Http;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Single;

import static com.smartdengg.ultra.Utils.getJsonFromMap;

public class RequestEntity<Source> {

  /** Log tag, apps may override it. */
  public static String TAG = RequestEntity.class.getSimpleName();

  private Http.Type type;
  private String classAnnotationUrl;
  private String fieldAnnotationUrl;
  private Map<String, String> params;
  private List<String> headers;
  private boolean loggable;

  private Source source;

  RequestEntity() {
  }

  public Http.Type getType() {
    return type;
  }

  RequestEntity setType(Http.Type type) {
    this.type = type;
    return RequestEntity.this;
  }

  public String getUrl() {
    if (fieldAnnotationUrl == null) return classAnnotationUrl;
    return fieldAnnotationUrl;
  }

  RequestEntity setUrlFromClassAnno(String url) {
    this.classAnnotationUrl = url;
    return RequestEntity.this;
  }

  String setUrlFromFieldAnno(String url) {
    String temp = fieldAnnotationUrl;
    this.fieldAnnotationUrl = url;
    return temp;
  }

  public Map<String, String> getParams() {
    return new HashMap<>(params);
  }

  RequestEntity setParams(Map<String, String> params) {
    this.params = params;
    return RequestEntity.this;
  }

  public List<String> getHeaders() {
    if (headers == null || headers.isEmpty()) return Collections.emptyList();
    return new ArrayList<>(headers);
  }

  RequestEntity setHeaders(List<String> headers) {
    this.headers = headers;
    return RequestEntity.this;
  }

  public Source getSource() {
    return source;
  }

  void setSource(Source source) {
    this.source = source;
  }

  boolean isLoggable() {
    return loggable;
  }

  RequestEntity setLoggable(boolean loggable) {
    this.loggable = loggable;
    return RequestEntity.this;
  }

  public Observable<RequestEntity<Source>> asObservable() {
    return Observable.just(RequestEntity.this);
  }

  public Single<RequestEntity<Source>> asSingle() {
    return Single.just(RequestEntity.this);
  }

  void dump() {

    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("http", type);
      jsonObject.put("url", fieldAnnotationUrl != null ? fieldAnnotationUrl : classAnnotationUrl);
      jsonObject.putOpt("headers", Arrays.toString(headers.toArray()));
      jsonObject.putOpt("params", getJsonFromMap(params));
    } catch (JSONException ignored) {
      return;
    }
    JsonPrinter.d(TAG, jsonObject.toString());
  }

  @Override public String toString() {
    return "RequestEntity{"
        + "type="
        + type
        + ", ur='"
        + (fieldAnnotationUrl != null ? fieldAnnotationUrl : classAnnotationUrl)
        + '\''
        + ", params="
        + params
        + ", headers="
        + Arrays.toString(headers.toArray())
        + ", loggable="
        + loggable
        + ", source="
        + source
        + '}';
  }
}
