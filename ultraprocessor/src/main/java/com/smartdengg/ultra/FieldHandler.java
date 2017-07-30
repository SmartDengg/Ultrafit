package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.Url;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间: 2017/07/26 16:33 <br>
 * 作者: dengwei <br>
 * 描述:
 */
class FieldHandler<Source> extends UltraHandler<Source> {

  private FieldHandler() {
  }

  static <Request> UltraHandler<Request> create() {
    return new FieldHandler<>();
  }

  @Override void process(RequestEntity<Source> requestEntity, Source request) throws Exception {

    final Map<String, String> parameters = new HashMap<>();
    Class<?> clazz = request.getClass();

    do {
      if (Object.class.getName().equalsIgnoreCase(clazz.getName())) break;

      Field[] declaredFields = clazz.getDeclaredFields();
      recursionParams(requestEntity, declaredFields, request, parameters);

      clazz = clazz.getSuperclass();
    } while (clazz != null);

    requestEntity.setParams(parameters);
  }

  private static <T> void recursionParams(RequestEntity<?> requestEntity, Field[] fields, T request,
      Map<String, String> parameters) {

    if (fields == null || fields.length == 0) return;
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = fields.length; i < n; i++) {
      final Field field = fields[i];
      if (!field.isAnnotationPresent(Argument.class) && !field.isAnnotationPresent(Url.class)) {
        continue;
      }

      if (!Modifier.isPublic(field.getModifiers())) field.setAccessible(true);

      Annotation[] annotations = field.getAnnotations();
      //noinspection ForLoopReplaceableByForEach
      for (int j = 0, k = annotations.length; j < k; j++) {

        final Annotation annotation = annotations[j];

        if (annotation instanceof Argument) {

          String parameterName;
          String parameterValue;

          parameterName = ((Argument) annotation).parameter();
          try {
            final Object fieldValue = field.get(request);
            parameterValue = Utils.getValue(fieldValue);
          } catch (IllegalAccessException e) {
            throw Utils.fieldError(e, field, "IllegalAccessException was happened");
          }

          if (parameterValue == null) continue; // Skip null values.
          if (parameters.containsKey(parameterName)) {
            parameterValue = parameters.get(parameterName) + "," + parameterValue;
          }
          if (parameterName.trim().isEmpty()) parameterName = field.getName();

          parameters.put(parameterName, parameterValue);
        } else if (annotation instanceof Url) {

          String url;

          Class<?> fieldClass = field.getType();
          if (fieldClass == String.class || fieldClass == URI.class || (fieldClass != null
              && "android.net.Uri".equals(fieldClass.getName()))) {
            try {
              url = field.get(request).toString();
              if (url == null) continue; // Skip null values.
              String previousUrl = requestEntity.setUrlFromFieldAnno(url);
              if (previousUrl != null) {
                throw Utils.fieldError(null, field, "Multiple @Url method annotations found.");
              }
            } catch (IllegalAccessException e) {
              throw Utils.fieldError(e, field, "IllegalAccessException was happened");
            }
          } else {
            throw Utils.fieldError(null, field,
                "@Url must be String, java.net.URI, or android.net.Uri type.");
          }
        }
      }
    }
  }
}

