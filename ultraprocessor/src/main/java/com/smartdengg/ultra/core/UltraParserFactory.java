package com.smartdengg.ultra.core;

import java.lang.reflect.Modifier;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory<R> {

  private static final String TAG = UltraParserFactory.class.getSimpleName();

  private R request;

  @SuppressWarnings("unchecked")
  public static <R> UltraParserFactory createParser(R request) {
    return new UltraParserFactory(Utils.checkNotNull(request, "request == null"));
  }

  private UltraParserFactory(R request) {
    if (Modifier.isInterface(
        Utils.checkNotNull(request, "request == null").getClass().getModifiers())) {
      throw Utils.methodError(request.getClass(), "Only class can be parsed,%s is a Interface",
          request.getClass().getSimpleName());
    }
    this.request = request;
  }

  public RequestEntity<R> parseRequestEntity() {
    return new RequestBuilder<>(request).build();
  }
}
