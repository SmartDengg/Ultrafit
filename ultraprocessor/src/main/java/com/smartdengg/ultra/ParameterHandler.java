package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.Argument;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间: 2017/07/26 16:33 <br>
 * 作者: dengwei <br>
 * 描述:
 */
class ParameterHandler<T> extends UltraHandler<T> {

  private ParameterHandler() {
  }

  static <Request> UltraHandler<Request> create() {
    return new ParameterHandler<>();
  }

  @Override void process(RequestEntity<T> requestEntity, T request) {

    final Map<String, String> parameters = new HashMap<>();
    Class<?> clazz = request.getClass();

    do {
      if (Object.class.getName().equalsIgnoreCase(clazz.getName())) break;

      Field[] declaredFields = clazz.getDeclaredFields();
      hunterParams(declaredFields, request, parameters);

      clazz = clazz.getSuperclass();
    } while (clazz != null);

    requestEntity.setParamMap(parameters);
  }

  private static <T> void hunterParams(Field[] fields, T request, Map<String, String> parameters) {

    if (fields == null || fields.length == 0) return;
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = fields.length; i < n; i++) {
      final Field field = fields[i];
      if (!field.isAnnotationPresent(Argument.class)) return;

      String key;
      Object raw;
      String value;

      if (!Modifier.isPublic(field.getModifiers())) field.setAccessible(true);

      key = field.getAnnotation(Argument.class).parameter();
      try {
        raw = field.get(request);
        if (raw == null) continue;
      } catch (IllegalAccessException e) {
        throw Utils.methodError(field.getDeclaringClass(),
            "IllegalAccessException was happened when access " + "%s field", field.getName());
      }

      value = Utils.getValue(raw);
      if (parameters.containsKey(key)) value = parameters.get(key) + "," + value;
      if (key.trim().isEmpty()) key = field.getName();

      parameters.put(key, value);
    }
  }
}

