package com.example.common.ultrafit;

import android.support.annotation.NonNull;
import com.example.common.ultrafit.annotation.Argument;
import com.example.common.ultrafit.annotation.RestMethod;
import com.example.common.ultrafit.type.RestType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParser {

  private Object rawEntity;
  private RequestEntity requestEntity;
  private Class<?> clazz;

  private UltraParser(Object rawEntity) {
    this.rawEntity = rawEntity;
    this.requestEntity = new RequestEntity();
  }

  public static UltraParser createParser(Object rawEntity) {
    return new UltraParser(rawEntity);
  }

  public String parseUrl() {
    UltraParser.this.installRestUrl();
    return requestEntity.getUrl();
  }

  private void installRestUrl() {
    RequestEntity tempEntity = UltraParser.this.internalParseUrl();
    requestEntity.setRestType(tempEntity.getRestType()).setUrl(tempEntity.getUrl());
  }

  public Map<String, String> parseParameter() {
    UltraParser.this.installParams();
    return requestEntity.getQueryMap();
  }

  private void installParams() {
    RequestEntity tempEntity = UltraParser.this.internalParseParameter();
    requestEntity.setQueryMap(tempEntity.getQueryMap());
  }

  public RequestEntity parseRequestEntity() {

    if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
      UltraParser.this.installRestUrl();
    }

    if (requestEntity.getQueryMap() == null) {
      UltraParser.this.installParams();
    }

    return requestEntity;
  }

  @NonNull private RequestEntity internalParseUrl() {

    RestType restType = null;
    String url = null;

    clazz = rawEntity.getClass();
    Annotation[] annotations = clazz.getAnnotations();

    for (Annotation classAnnotation : annotations) {

      Class<? extends Annotation> annotationType = classAnnotation.annotationType();
      RestMethod restMethod = null;

      for (Annotation innerAnnotation : annotationType.getAnnotations()) {
        if (innerAnnotation instanceof RestMethod) {
          restMethod = (RestMethod) innerAnnotation;
          break;
        }
      }

      if (restMethod != null) {
        if (restType != null) {
          throw Errors.methodError(clazz,
                                   "Only one HTTP method is allowed.Found: %s and %s.You should choose one from these.",
                                   restType.name(), restMethod.type());
        }

        try {
          url = (String) annotationType.getMethod("stringUrl").invoke(classAnnotation);
        } catch (Exception ignored) {
          throw Errors.methodError(clazz, "Failed to extract String 'value' from @%s annotation.",
                                   annotationType.getSimpleName());
        }
        restType = restMethod.type();
      }
    }

    if (restType == null) {
      throw Errors.methodError(clazz, "HTTP method annotation is required (e.g., @HttpGet, @HttpPost, etc.).");
    }

    if (url == null) {
      throw Errors.methodError(clazz, "RestType is required (e.g., HttpGet, " + "HttpPost, etc" + ".).");
    }
    return new RequestEntity(restType, url, null);
  }

  public RequestEntity internalParseParameter() {

    if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
      throw Errors.methodError(clazz, "You should first invoke parseUrl() before call this method.");
    }

    Map<String, String> params = new HashMap<>();
    Class<?> clazz = rawEntity.getClass();

    Field[] declaredFields = clazz.getDeclaredFields();

    for (Field field : declaredFields) {

      field.setAccessible(true);

      if (field.isAnnotationPresent(Argument.class)) {

        Argument argument = field.getAnnotation(Argument.class);
        Class<?> type = field.getType();

        String name = null;
        String value = null;

        if (type == String.class) { /** String */
          try {
            name = argument.parameter();
            value = (String) field.get(rawEntity);
          } catch (IllegalAccessException ignored) {
          }
        } else if (type == String[].class) { /** String[] */
          try {
            name = argument.parameter();
            value = Arrays.toString(((String[]) field.get(rawEntity)));
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        } else if (type == Integer[].class || type == int[].class) {/** Integer[] and int[] */
          try {
            name = argument.parameter();
            Object object = field.get(rawEntity);

            if (object instanceof int[]) {
              value = Arrays.toString(((int[]) object));
            } else {
              value = Arrays.toString(((Integer[]) object));
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        } else if (type == Double[].class || type == double[].class) {/** Double[] and double[] */
          try {
            name = argument.parameter();
            Object object = field.get(rawEntity);

            if (object instanceof double[]) {
              value = Arrays.toString(((double[]) object));
            } else {
              value = Arrays.toString(((Double[]) object));
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        } else {
          try {
            name = argument.parameter();
            value = field.get(rawEntity).toString();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }

        if (params.containsKey(name)) {
          throw Errors.methodError(field.getDeclaringClass(),
                                   "The parameter %s at least already exists one.You must choose one "
                                       + "from these which value is '%s'"
                                       + " or"
                                       + " '%s'", name, params.get(name), value);
        }
        params.put(name, value);
      }
    }
    return new RequestEntity(null, null, Collections.unmodifiableMap(params));
  }
}
