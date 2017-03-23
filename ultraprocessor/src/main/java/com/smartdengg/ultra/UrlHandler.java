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
  private boolean logFlag = true;

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
      final Class<? extends Annotation> annotationClazz = annotation.annotationType();
      if (!annotationClazz.isAnnotationPresent(HttpType.class)) continue;
      final HttpType httpTypeAnnotation = annotationClazz.getAnnotation(HttpType.class);

      if (httpType != null) {
        String otherUrl = Reflections.invokeAnnotation(annotation, RequestEntityBuilder.HTTP_URL);
        throw Utils.methodError(clazz,
            "Only one HTTP type is allowed!\n Found: %s: '%s' and %s: '%s'!", this.httpType,
            this.url, httpTypeAnnotation.type(), otherUrl);
      }

      /*Only HttpGet or HttpPost*/
      this.httpType = httpTypeAnnotation.type();
      this.url = Reflections.invokeAnnotation(annotation, RequestEntityBuilder.HTTP_URL);
      this.logFlag = Reflections.invokeAnnotation(annotation, RequestEntityBuilder.LOG_FLAG);
    }

    if (this.httpType == null || this.url == null) {
      throw Utils.methodError(clazz,
          "Http method annotation is required (e.g.@HttpGet, @HttpPost, etc.).");
    }

    requestEntity.setType(httpType).setUrl(url).setShouldOutputs(logFlag);
  }
}
