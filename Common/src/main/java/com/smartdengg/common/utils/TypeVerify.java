package com.smartdengg.common.utils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * 创建时间:  2016/11/09 15:26 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class TypeVerify {

  public static void verifyGenericArrayType(Type type) {

    if (type instanceof GenericArrayType) {
      System.out.println(type + " is a GenericArrayType!");
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      System.out.println("GenericArrayType's componentType is : " + componentType);

      //
      verifyParameterizedType(componentType);

      //
      verifyTypeVariable(componentType);
    }
  }

  public static void verifyParameterizedType(Type type) {

    if (type instanceof ParameterizedType) {
      System.out.println(type + " is a ParameterizedType!");
      ParameterizedType parameterizedType = (ParameterizedType) type;
      System.out.println("It's raw type is : " + parameterizedType.getRawType());
      System.out.println("It's owner type is : " + parameterizedType.getOwnerType());

      for (Type actualType : parameterizedType.getActualTypeArguments()) {
        System.out.println("Actual Type is : " + actualType);

        //
        verifyTypeVariable(actualType);

        //
        verifyTypeWildcardType(type);
      }
    }
  }

  public static void verifyTypeVariable(Type type) {

    if (type instanceof TypeVariable) {
      System.out.println(type + " is a TypeVariable!");
      Type[] upperBounds = ((TypeVariable) type).getBounds();
      for (Type upperBoundType : upperBounds) {
        System.out.println("upperBound is " + upperBoundType);
      }
    }
  }

  public static void verifyTypeWildcardType(Type type) {
    if (type instanceof WildcardType) {
      System.out.println(type + " is a WildcardType!");
      Type[] upperBounds = ((WildcardType) type).getUpperBounds();
      for (Type upperBoundType : upperBounds) {
        System.out.println("upperBound is " + upperBoundType);
      }
      Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
      for (Type lowerBoundType : lowerBounds) {
        System.out.println("lowerBounds is " + lowerBoundType);
      }
    }
  }

  public static void verifyClass(Type type) {
    if (type instanceof Class) {
      System.out.println("type is a class : " + type);
      Class clazz = (Class) type;
      System.out.println("It's name is : " + clazz.getCanonicalName());
    }
  }

  public static void verifyArray(Type type) {
    if (type instanceof Class) {
      Class clazz = (Class) type;
      if (clazz.isArray()) {
        System.out.println("type is a array : " + type);
        Class<?> componentType = clazz.getComponentType();
        System.out.println("The component type of this array is : " + componentType);
      }
    }
  }
}
