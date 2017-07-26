package com.smartdengg.ultra;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by SmartDengg on 2016/4/24.
 */
class Reflections {

  public static boolean hasDefaultConstructor(Class<?> clazz) throws SecurityException {
    Class<?>[] empty = {};
    try {
      clazz.getConstructor(empty);
    } catch (NoSuchMethodException e) {
      return false;
    }
    return true;
  }

  static Constructor getConstructor(String className, Class<?>... parameterTypes) {

    Constructor constructor = null;

    try {
      Class<?> clazz = Class.forName(className);
      constructor = clazz.getDeclaredConstructor(parameterTypes);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return constructor;
  }

  @SuppressWarnings("unchecked")
  public static Object newInstance(Constructor constructor, Object... parameters) {

    Object instance = null;

    try {
      if (!Modifier.isPublic(constructor.getModifiers())) constructor.setAccessible(true);
      instance = constructor.newInstance(parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return instance;
  }

  @SuppressWarnings("unchecked")
  public static Method getDeclaredMethod(Class clazz, String name, Class<?>... parameterTypes) {

    Method declaredMethod = null;
    try {
      declaredMethod = clazz.getDeclaredMethod(name, parameterTypes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return declaredMethod;
  }

  public static Object invokeMethod(Object instance, Method method, Object... parameters) {

    Object returnObject = null;

    try {
      if (!Modifier.isPublic(method.getModifiers())) method.setAccessible(true);
      returnObject = method.invoke(instance, parameters);
    } catch (Exception ignored) {
    }

    return returnObject;
  }

  /** Safe because of generics erasure */
  @SuppressWarnings("unchecked") static <T> T invokeAnnotation(Object instance, String methodName) {
    try {
      Class<? extends Annotation> clazz = ((Annotation) instance).annotationType();
      Method declaredMethod = clazz.getDeclaredMethod(methodName);
      if (!Modifier.isPublic(declaredMethod.getModifiers())) declaredMethod.setAccessible(true);
      return (T) declaredMethod.invoke(instance);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  static String getMethodNameByReturnType(Class clazz, Class returnType, String defaultName) {

    Method[] methods = clazz.getDeclaredMethods();

    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = methods.length; i < n; i++) {

      Method method = methods[i];
      if (method.isSynthetic() || method.isBridge()) continue;
      if (method.getReturnType() == returnType) return method.getName();
    }

    return defaultName;
  }
}
