package com.smartdengg.ultra;

import com.smartdengg.jsonprinter.JsonPrinter;
import com.smartdengg.ultra.annotation.Type;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Single;

import static com.smartdengg.ultra.Utils.getJsonFromMap;

public class RequestEntity<R> {

  /** Log tag, apps may override it. */
  public static String TAG = RequestEntity.class.getSimpleName();

  private Type type;
  private String url;
  private Map<String, String> paramMap;

  private R sourceRequest;

  private boolean shouldOutputs;

  RequestEntity() {
  }

  public Type getType() {
    return type;
  }

  RequestEntity setType(Type type) {
    this.type = type;
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

  public R getRequest() {
    return sourceRequest;
  }

  void setRequest(R sourceEntity) {
    this.sourceRequest = sourceEntity;
  }

  boolean isShouldOutputs() {
    return shouldOutputs;
  }

  RequestEntity setShouldOutputs(boolean shouldOutputs) {
    this.shouldOutputs = shouldOutputs;
    return RequestEntity.this;
  }

  public Observable<RequestEntity<R>> asObservable() {
    return Observable.just(RequestEntity.this);
  }

  public Single<RequestEntity<R>> asSingle() {
    return Single.just(RequestEntity.this);
  }

  void dump() {

    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("http", type);
      jsonObject.put("url", url);
      jsonObject.putOpt("paramMap", getJsonFromMap(paramMap));
    } catch (JSONException ignored) {
    }
    JsonPrinter.d(TAG, jsonObject.toString());
  }

  @Override public String toString() {
    return "RequestEntity{"
        + "type="
        + type
        + ", url='"
        + url
        + '\''
        + ", paramMap="
        + paramMap
        + ", sourceRequest="
        + sourceRequest
        + ", shouldOutputs="
        + shouldOutputs
        + '}';
  }
}
