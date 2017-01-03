package com.smartdengg.ultra;

/**
 * Created by SmartDengg on 2016/6/25.
 */
class RequestBuilder<Request> {

  static final String HTTP_METHOD = "stringUrl";
  static final String LOG_FLAG = "LOG";

  private Request mRequest;

  RequestEntity<Request> requestEntity = new RequestEntity<>();

  RequestBuilder(Request Request) {
    this.mRequest = Request;
  }

  public RequestEntity<Request> build() {

    UrlHandler.apply(RequestBuilder.this, mRequest);
    ParameterHandler.apply(RequestBuilder.this, mRequest);
    requestEntity.setSourceRequest(mRequest);

    if (requestEntity.isShouldOutputs()) this.outputs();

    return requestEntity;
  }

  private void outputs() {
    Utils.checkNotNull(requestEntity, "requestEntity == null").dump();
  }
}
