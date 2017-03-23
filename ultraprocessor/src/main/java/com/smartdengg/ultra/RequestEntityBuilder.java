package com.smartdengg.ultra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SmartDengg on 2016/6/25.
 */
class RequestEntityBuilder<Request> {

  static final String HTTP_URL = "url";
  static final String LOG_FLAG = "LOG";

  private Request request;
  private List<UltraHandler<Request>> builtInHandlers;
  private final RequestEntity<Request> requestEntity = new RequestEntity<>();

  RequestEntityBuilder(Request Request) {
    this.request = Request;
    List<UltraHandler<Request>> handlers = new ArrayList<>(2);
    handlers.add(UrlHandler.<Request>create());
    handlers.add(ParameterHandler.<Request>create());
    this.builtInHandlers = Collections.unmodifiableList(handlers);
  }

  public RequestEntity<Request> build() {

    try {
      for (int i = 0, n = builtInHandlers.size(); i < n; i++) {
        final UltraHandler<Request> handler = builtInHandlers.get(i);
        handler.process(requestEntity, request);
      }
    } catch (Throwable ignored) {
    }

    requestEntity.setRequest(request);

    if (requestEntity.isShouldOutputs()) this.outputs();

    return requestEntity;
  }

  private void outputs() {
    Utils.checkNotNull(requestEntity, "requestEntity == null").dump();
  }
}
