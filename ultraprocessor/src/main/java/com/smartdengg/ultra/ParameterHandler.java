package com.smartdengg.ultra;

import com.smartdengg.ultra.annotation.Argument;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joker on 2016/6/28.
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
      Object rawValue;
      String covertValue;

      if (!Modifier.isPublic(field.getModifiers())) field.setAccessible(true);

      key = field.getAnnotation(Argument.class).parameter();
      try {
        rawValue = field.get(request);
        if (rawValue == null) continue;
      } catch (IllegalAccessException e) {
        throw Utils.methodError(field.getDeclaringClass(),
            "IllegalAccessException was happened when access " + "%s field", field.getName());
      }

      covertValue = Utils.toString(rawValue, Types.getRawType(field.getType()));
      if (parameters.containsKey(key)) covertValue = parameters.get(key) + "," + covertValue;
      if (key.trim().isEmpty()) key = field.getName();

      parameters.put(key, covertValue);
    }
  }
}

