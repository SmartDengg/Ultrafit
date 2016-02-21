package com.smartdengg.ultrafit.service.utils;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/**
 * Created by Joker on 2016/2/19.
 * thanks to square,best wishes 4 u.
 */
public class Types {

  public static Class<?> getRawType(Type type) {

    if (type instanceof Class<?>) {
      // Type is a normal class.
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
      // suspects some pathological case related to nested classes exists.
      Type rawType = parameterizedType.getRawType();
      if (!(rawType instanceof Class)) throw new IllegalArgumentException();
      return (Class<?>) rawType;
    } else if (type instanceof GenericArrayType) {

      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    } else if (type instanceof TypeVariable) {

      // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
      // type that's more general than necessary is okay.
      return Object.class;
    } else if (type instanceof WildcardType) {

      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    } else {

      String className = type == null ? "null" : type.getClass().getName();
      throw new IllegalArgumentException(
          "Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" + type + "> is of type " + className);
    }
  }

 public static Type getParameterUpperBound(int index, ParameterizedType type) {
    Type[] types = type.getActualTypeArguments();
    if (types.length <= index) {
      throw new IllegalArgumentException(
          "Expected at least " + index + " type argument(s) but got: " + Arrays.toString(types));
    }
    Type paramType = types[index];
    if (paramType instanceof WildcardType) {
      return ((WildcardType) paramType).getUpperBounds()[0];
    }
    return paramType;
  }

}
