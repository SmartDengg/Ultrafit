package com.example.common.ultrafit;

import android.support.annotation.NonNull;
import com.example.common.Constants;
import com.example.common.ultrafit.annotation.Argument;
import com.example.common.ultrafit.annotation.RestMethod;
import com.example.common.ultrafit.type.RestType;
import com.example.common.ultrafit.type.Types;
import com.orhanobut.logger.Logger;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory {

  public static void outputs(@NonNull RequestEntity requestEntity) {

    Logger.d("Request entity !!!!" +
                 "\n  ⇢ " +
                 " Type   : " +
                 requestEntity.getRestType().name() +
                 "\n  ⇢ " +
                 " Url    : " +
                 Constants.BASE_URL + requestEntity.getUrl() +
                 "\n  ⇢ " +
                 " Params : " +
                 requestEntity.getParamMap());
  }

  private Object rawEntity;
  private RequestEntity requestEntity;
  private Class<?> clazz;

  private UltraParserFactory(Object rawEntity) {
    this.rawEntity = rawEntity;
    this.requestEntity = new RequestEntity();
  }

  public static UltraParserFactory createParser(Object rawEntity) {
    return new UltraParserFactory(rawEntity);
  }

  public String parseUrl() {
    UltraParserFactory.this.parseRestUrl();
    return requestEntity.getUrl();
  }

  private void parseRestUrl() {
    RequestEntity tempEntity = UltraParserFactory.this.internalParseUrl();
    requestEntity.setRestType(tempEntity.getRestType()).setUrl(tempEntity.getUrl());
  }

  public Map<String, String> parseParameter() {
    UltraParserFactory.this.parseParams();
    return requestEntity.getParamMap();
  }

  private void parseParams() {
    RequestEntity tempEntity = UltraParserFactory.this.internalParseParameter();
    requestEntity.setParamMap(tempEntity.getParamMap());
  }

  public RequestEntity parseRequestEntity() {

    if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
      UltraParserFactory.this.parseRestUrl();
    }

    if (requestEntity.getParamMap() == null) {
      UltraParserFactory.this.parseParams();
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
        Class<?> parameterType = field.getType();
        Class<?> rawParameterType = Types.getRawType(parameterType);

        String name;
        String value;
        Object object;

        try {
          object = field.get(rawEntity);
        } catch (IllegalAccessException e) {
          throw Errors.methodError(field.getDeclaringClass(),
                                   "IllegalAccessException was happened when access " + " %s field", field.getName());
        }

        if (object == null) continue;

        if (rawParameterType.isArray()) {
          Class<?> arrayComponentType = UltraParserFactory.this.boxIfPrimitive(rawParameterType.getComponentType());
          name = argument.parameter();
          value = UltraParserFactory.this.arrayToString(object, arrayComponentType);
        } else {
          name = argument.parameter();
          value = object.toString();
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

  private Class<?> boxIfPrimitive(Class<?> type) {
    if (boolean.class == type) return Boolean.class;
    if (byte.class == type) return Byte.class;
    if (char.class == type) return Character.class;
    if (double.class == type) return Double.class;
    if (float.class == type) return Float.class;
    if (int.class == type) return Integer.class;
    if (long.class == type) return Long.class;
    if (short.class == type) return Short.class;
    return type;
  }

  private String arrayToString(Object object, Class<?> rawParameterType) {

    String value;

    if (rawParameterType == Boolean.class) {/** Boolean[] */
      try {
        value = Arrays.toString(((Boolean[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((boolean[]) object));
      }
    } else if (rawParameterType == Byte.class) {/** Byte[] */
      try {
        value = Arrays.toString(((Byte[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((byte[]) object));
      }
    } else if (rawParameterType == Character.class) {/** Character[] */
      try {
        value = Arrays.toString(((Character[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((char[]) object));
      }
    } else if (rawParameterType == Double.class) {/** Double[] */
      try {
        value = Arrays.toString(((Double[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((double[]) object));
      }
    } else if (rawParameterType == Float.class) {/** Float[] */
      try {
        value = Arrays.toString(((Float[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((float[]) object));
      }
    } else if (rawParameterType == Integer.class) {/** Integer[] */
      try {
        value = Arrays.toString(((Integer[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((int[]) object));
      }
    } else if (rawParameterType == Long.class) {/** Long[] */
      try {
        value = Arrays.toString(((Long[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((long[]) object));
      }
    } else if (rawParameterType == Short.class) {/** Short[] */
      try {
        value = Arrays.toString(((Short[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((short[]) object));
      }
    } else {
      value = object != null ? Arrays.toString((Object[]) object) : null;
    }

    return value;
  }
}
