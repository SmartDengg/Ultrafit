package com.smartdengg.ultra;

import com.smartdengg.ultra.internal.AnnotationType;
import com.smartdengg.ultra.annotation.Headers;
import com.smartdengg.ultra.annotation.Http;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.smartdengg.ultra.Reflections.invokeAnnotation;

/**
 * 创建时间: 2017/03/23 11:46 <br>
 * 作者: dengwei <br>
 * 描述:
 */
class ClassHandler<T> extends UltraHandler<T> {

  private Http.Type httpType = null;
  private String url = null;
  private boolean loggable = true;
  private List<String> headers;

  private ClassHandler() {
  }

  static <Request> UltraHandler<Request> create() {
    return new ClassHandler<>();
  }

  @Override void process(RequestEntity<T> requestEntity, T request) throws Exception {

    Class<?> clazz = request.getClass();
    Annotation[] annotations = clazz.getAnnotations();
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = annotations.length; i < n; i++) {

      final Annotation annotation = annotations[i];
      final Class<? extends Annotation> annotationClass = annotation.annotationType();
      if (!annotationClass.isAnnotationPresent(AnnotationType.class)) continue;

      if (annotationClass == Http.class) {

        if (httpType != null) {
          throw Utils.classError(null, clazz, "Multiple @Http method annotations found.");
        }

        /*Only Get or Post*/
        this.httpType = invokeAnnotation(annotation, getTypeName(annotationClass));
        this.url = invokeAnnotation(annotation, getUrlMethodName(annotationClass));
        this.loggable = invokeAnnotation(annotation, getLogMethodName(annotationClass));
      } else if (annotationClass == Headers.class) {

        String[] headers =
            Reflections.invokeAnnotation(annotation, getHeadersName(annotationClass));
        for (String header : headers) {
          int colon = header.indexOf(':');
          if (colon == -1 || colon == 0 || colon == header.length() - 1) {
            throw Utils.classError(null, clazz,
                "@Headers value must be in the form \"Name: Value\". Found: \"%s\"", header);
          }
        }
        this.headers = new ArrayList<>(Arrays.asList(headers));
      }
    }

    if (this.httpType == null) {
      throw Utils.classError(null, clazz,
          "@Http annotation is required (e.g.@Http(GET), @Http(POST), etc.).");
    }

    requestEntity.setType(httpType)
        .setUrlFromClassAnno(url)
        .setHeaders(headers)
        .setLoggable(loggable);
  }

  private static String getUrlMethodName(Class clazz) {
    return Reflections.getMethodNameByReturnType(clazz, String.class, RequestEntityBuilder.URL);
  }

  private static String getLogMethodName(Class clazz) {
    return Reflections.getMethodNameByReturnType(clazz, boolean.class,
        RequestEntityBuilder.LOGGABLE);
  }

  private static String getTypeName(Class clazz) {
    return Reflections.getMethodNameByReturnType(clazz, Http.Type.class,
        RequestEntityBuilder.DEFAULT_VALUE);
  }

  private static String getHeadersName(Class clazz) {
    return Reflections.getMethodNameByReturnType(clazz, String[].class,
        RequestEntityBuilder.DEFAULT_VALUE);
  }
}
