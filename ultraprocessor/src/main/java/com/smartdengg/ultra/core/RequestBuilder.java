package com.smartdengg.ultra.core;

/**
 * Created by SmartDengg on 2016/6/25.
 */
class RequestBuilder<R> {

  static final String HTTP_METHOD = "stringUrl";
  static final String LOG_FLAG = "LOG";

  private R request;

  RequestEntity<R> requestEntity = new RequestEntity<>();

  RequestBuilder(R request) {
    this.request = request;
  }

  public RequestEntity<R> build() {

    UrlHandler.handler(RequestBuilder.this, request);
    ParameterHandler.handler(RequestBuilder.this, request);
    this.requestEntity.setSourceRequest(request);

    if (this.requestEntity.isShouldOutputs()) this.outputs();

    return this.requestEntity;
  }

  private void outputs() {
    System.out.println(Utils.checkNotNull(requestEntity, "requestEntity == null").toString());
  }
}
