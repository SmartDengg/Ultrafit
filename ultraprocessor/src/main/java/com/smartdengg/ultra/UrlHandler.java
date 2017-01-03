package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.RestMethod;
import com.smartdengg.ultra.annotation.RestType;
import java.lang.annotation.Annotation;

/**
 * Created by Joker on 2016/6/28.
 */
class UrlHandler extends UltraHandler<Annotation[]> {

  private static Class<?> CLASS;

  private RestType restType = null;
  private String url = null;
  private boolean logFlag = true;

  private UrlHandler() {
  }

  static <T> void apply(RequestBuilder builder, T request) {

    CLASS = request.getClass();

    new UrlHandler().process(builder, request.getClass().getAnnotations());
  }

  @Override void process(RequestBuilder builder, Annotation[] annotations) {

    for (Annotation httpAnnotation : annotations) {

      Class<? extends Annotation> clazz = httpAnnotation.annotationType();
      if (!clazz.isAnnotationPresent(RestMethod.class)) continue;

      RestMethod restMethod = clazz.getAnnotation(RestMethod.class);

      if (this.restType != null) {
        String previousUrl =
            Reflections.invokeMethod(httpAnnotation, clazz, RequestBuilder.HTTP_METHOD);
        throw Utils.methodError(CLASS,
            "Only one HTTP method is allowed!\n Found: %s: '%s' or %s: '%s'!", this.restType,
            this.url, restMethod.type(), previousUrl);
      }

      /*Only HttpGet or HttpPost*/
      this.restType = restMethod.type();
      this.url = Reflections.invokeMethod(httpAnnotation, clazz, RequestBuilder.HTTP_METHOD);
      this.logFlag = Reflections.invokeMethod(httpAnnotation, clazz, RequestBuilder.LOG_FLAG);
    }

    if (this.restType == null || this.url == null) {
      throw Utils.methodError(CLASS,
          "Http method annotation is required (e.g.@HttpGet, @HttpPost, etc.).");
    }

    builder.requestEntity.setRestType(restType).setUrl(url).setShouldOutputs(logFlag);
  }
}
