package com.smartdengg.ultra.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by SmartDengg on 2016/4/24.
 */
public class Reflections {

  public static boolean hasDefaultConstructor(Class<?> clazz) throws SecurityException {
    Class<?>[] empty = {};
    try {
      clazz.getConstructor(empty);
    } catch (NoSuchMethodException e) {
      return false;
    }
    return true;
  }

  public static Constructor getConstructor(String className, Class<?>... parameterTypes) {

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

  @SuppressWarnings("unchecked") public static Method getDeclaredMethod(Class clazz, String name, Class<?>... parameterTypes) {

    Method declaredMethod = null;
    try {
      declaredMethod = clazz.getDeclaredMethod(name, parameterTypes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return declaredMethod;
  }

  static Object invokeMethod(Method method, Object instance, Object... parameters) {

    Object returnObject = null;

    try {
      if (!Modifier.isPublic(method.getModifiers())) method.setAccessible(true);
      returnObject = method.invoke(instance, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return returnObject;
  }

  /** Safe because of generics erasure */
  @SuppressWarnings("unchecked") static <T> T invokeMethod(Annotation classAnnotation,
      Class<? extends Annotation> clazz, String methodName) {
    try {
      Method declaredMethod = clazz.getDeclaredMethod(methodName);
      if (!Modifier.isPublic(declaredMethod.getModifiers())) declaredMethod.setAccessible(true);
      return (T) declaredMethod.invoke(classAnnotation);
    } catch (Exception ignore) {
      throw Utils.methodError(clazz, "Failed to extract String 'value' from @%s annotation.",
          clazz.getSimpleName());
    }
  }
}
