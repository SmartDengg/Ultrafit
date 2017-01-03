package com.smartdengg.ultra;

import java.lang.reflect.Modifier;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory<Request> {

  private Request mRequest;

  public static <Request> UltraParserFactory<Request> createWith(Request request) {
    if (Modifier.isInterface(
        Utils.checkNotNull(request, "mRequest == null").getClass().getModifiers())) {
      throw Utils.methodError(request.getClass(), "Only class can be parsed,%s is a Interface",
          request.getClass().getSimpleName());
    }
    return new UltraParserFactory<>(request);
  }

  private UltraParserFactory(Request request) {
    this.mRequest = request;
  }

  public RequestEntity<Request> parse() {
    return new RequestBuilder<>(mRequest).build();
  }
}
