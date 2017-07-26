package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.HttpType;
import com.smartdengg.ultra.annotation.Type;
import java.lang.annotation.Annotation;

/**
 * 创建时间: 2017/03/23 11:46 <br>
 * 作者: dengwei <br>
 * 描述:
 */
class UrlHandler<T> extends UltraHandler<T> {

  private Type httpType = null;
  private String url = null;
  private boolean loggable = true;

  private UrlHandler() {
  }

  static <Request> UltraHandler<Request> create() {
    return new UrlHandler<>();
  }

  @Override void process(RequestEntity<T> requestEntity, T request) throws Exception {

    Class<?> clazz = request.getClass();
    Annotation[] annotations = clazz.getAnnotations();
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = annotations.length; i < n; i++) {

      final Annotation annotation = annotations[i];
      final Class<? extends Annotation> annotationClass = annotation.annotationType();
      if (!annotationClass.isAnnotationPresent(HttpType.class)) continue;
      final HttpType httpTypeAnnotation = annotationClass.getAnnotation(HttpType.class);

      if (httpType != null) {
        String otherUrl = Reflections.invokeAnnotation(annotation, RequestEntityBuilder.HTTP_URL);
        throw Utils.methodError(clazz,
            "Only one HTTP type is allowed!\n Found: %s: '%s' and %s: '%s'!", this.httpType,
            this.url, httpTypeAnnotation.type(), otherUrl);
      }

      /*Only HttpGet or HttpPost*/
      this.httpType = httpTypeAnnotation.type();
      this.url = Reflections.invokeAnnotation(annotation, getUrlMethodName(annotationClass));
      this.loggable = Reflections.invokeAnnotation(annotation, getLogMethodName(annotationClass));
    }

    if (this.httpType == null || this.url == null) {
      throw Utils.methodError(clazz,
          "Http method annotation is required (e.g.@HttpGet, @HttpPost, etc.).");
    }

    requestEntity.setType(httpType).setUrl(url).setShouldOutputs(loggable);
  }

  private static String getUrlMethodName(Class clazz) {
    return Reflections.getMethodNameByReturnType(clazz, String.class,
        RequestEntityBuilder.HTTP_URL);
  }

  private static String getLogMethodName(Class clazz) {
    return Reflections.getMethodNameByReturnType(clazz, boolean.class,
        RequestEntityBuilder.LOGGABLE);
  }
}
