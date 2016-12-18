package com.smartdengg.ultra.core;

import com.smartdengg.ultra.annotation.Argument;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joker on 2016/6/28.
 */
class ParameterHandler<T> extends UltraHandler<T> {

  private Map<String, String> params = new HashMap<>();

  @SuppressWarnings("unchecked")
  static <Request> void apply(RequestBuilder builder, Request Request) {
    new ParameterHandler().process(builder, Request);
  }

  @Override void process(RequestBuilder builder, T request) {

    Class<?> clazz = request.getClass();
    Class<?> superClazz = clazz.getSuperclass();

    while (superClazz != null) {

      if (Object.class.getName().equalsIgnoreCase(superClazz.getName())) break;

      Field[] superFields = superClazz.getDeclaredFields();
      superClazz = superClazz.getSuperclass();

      ParameterHandler.this.hunterParams(superFields, request);
    }

    Field[] declaredFields = clazz.getDeclaredFields();
    ParameterHandler.this.hunterParams(declaredFields, request);

    builder.requestEntity.setParamMap(Collections.unmodifiableMap(this.params));
  }

  private void hunterParams(Field[] declaredFields, T request) {

    if (declaredFields == null || declaredFields.length == 0) return;

    for (Field field : declaredFields) {

      if (field.isAnnotationPresent(Argument.class)) {

        String key;
        Object value;
        String ultraValue;

        if (!Modifier.isPublic(field.getModifiers())) field.setAccessible(true);

        try {
          value = field.get(request);
        } catch (IllegalAccessException e) {
          throw Utils.methodError(field.getDeclaringClass(),
              "IllegalAccessException was happened when access " + "%s field", field.getName());
        }

        if (value == null) continue;

        key = field.getAnnotation(Argument.class).parameter();
        ultraValue = Utils.toString(value, Types.getRawType(field.getType()));

        if (this.params.containsKey(key)) {
          throw Utils.methodError(field.getDeclaringClass(),//
              "The parameter %s at least already exists one.You must choose one from these which value is '%s' or '%s'",
              //
              key, this.params.get(key), ultraValue);
        }

        if (key == null || key.trim().isEmpty()) key = field.getName();

        this.params.put(key, ultraValue);
      }
    }
  }
}
