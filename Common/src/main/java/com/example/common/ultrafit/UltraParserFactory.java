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
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory {

  private static final String HttpMethod = "stringUrl";

  public static void outputs(@NonNull RequestEntity requestEntity) {

    Logger.t(Constants.OKHTTP_TAG, 0).d("Request entity !!!!" +
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

  @NonNull
  private RequestEntity internalParseUrl() {

    RestType restType = null;
    String url = null;

    this.clazz = rawEntity.getClass();
    Annotation[] annotations = this.clazz.getAnnotations();

    for (Annotation classAnnotation : annotations) {

      Class<? extends Annotation> clazz = classAnnotation.annotationType();
      if (!clazz.isAnnotationPresent(RestMethod.class)) continue;

      RestMethod restMethod = clazz.getAnnotation(RestMethod.class);

      if (restType != null) {
        throw Errors.methodError(this.clazz,
                                 "Only one HTTP method is allowed.Found: %s and %s.You should choose one from these.",
                                 restType.name(), restMethod.type());
      }
      /*Only HttpGet or HttpPost*/
      restType = restMethod.type();

      try {
        url = (String) clazz.getMethod(HttpMethod).invoke(classAnnotation);
      } catch (Exception ignored) {
        throw Errors.methodError(this.clazz, "Failed to extract String 'value' from @%s annotation.",
                                 clazz.getSimpleName());
      }
    }

    if (restType == null || url == null) {
      throw Errors.methodError(this.clazz, "Http method annotation is required (e.g.@HttpGet, @HttpPost, etc.).");
    }

    return new RequestEntity(restType, url, null);
  }

  public RequestEntity internalParseParameter() {

    if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
      throw Errors.methodError(this.clazz, "You should first invoke parseUrl() before call this method.");
    }

    Map<String, String> params = new HashMap<>();
    Class<?> superClazz = this.clazz.getSuperclass();

    while (superClazz != null) {

      Field[] superFields = superClazz.getDeclaredFields();
      superClazz = superClazz.getSuperclass();

      if (superFields == null || superFields.length == 0) continue;
      UltraParserFactory.this.hunter(params, superFields);
    }

    Field[] subFields = this.clazz.getDeclaredFields();
    UltraParserFactory.this.hunter(params, subFields);

    return new RequestEntity(null, null, Collections.unmodifiableMap(params));
  }

  private void hunter(Map<String, String> params, Field[] declaredFields) {
    for (Field field : declaredFields) {

      if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);

      if (field.isAnnotationPresent(Argument.class)) {

        Argument argument = field.getAnnotation(Argument.class);
        Class<?> parameterType = field.getType();
        Class<?> rawParameterType = Types.getRawType(parameterType);

        String name;
        Object value;
        String ultra;

        try {
          value = field.get(rawEntity);
        } catch (IllegalAccessException e) {
          throw Errors.methodError(field.getDeclaringClass(),
                                   "IllegalAccessException was happened when access " + "%s field", field.getName());
        }

        if (value == null) continue;

        if (rawParameterType.isArray()) {
          Class<?> arrayComponentType = UltraParserFactory.this.boxIfPrimitive(rawParameterType.getComponentType());
          name = argument.parameter();
          ultra = UltraParserFactory.this.arrayToString(value, arrayComponentType);
        } else {
          name = argument.parameter();
          ultra = value.toString();
        }

        if (params.containsKey(name)) {
          throw Errors.methodError(field.getDeclaringClass(),
                                   "The parameter %s at least already exists one.You must choose one "
                                       + "from these which value is '%s'"
                                       + " or"
                                       + " '%s'", name, params.get(name), ultra);
        }
        params.put(name, ultra);
      }
    }
  }

  /**
   * If the type of this field is a primitive type, the field value is automatically boxed.
   *
   * @param type
   * @return
   */
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
