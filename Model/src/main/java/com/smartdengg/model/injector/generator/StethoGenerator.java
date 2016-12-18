package com.smartdengg.model.injector.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import okhttp3.Interceptor;

/**
 * Created by SmartDengg on 2016/5/16.
 */
class StethoGenerator {

  public static final boolean HAS_STETHO = hasStethoOnClasspath();
  public static final boolean HAS_STETHO_INTERCEPTOR = hasStethoInterceptorOnClasspath();

  private static final String stethoClassName = "com.facebook.stetho.Stetho";
  private static final String stethoInterceptorClassName =
      "com.facebook.stetho.okhttp3.StethoInterceptor";

  private StethoGenerator() {
    throw new AssertionError("No instance");
  }

  private static boolean hasStethoOnClasspath() {
    boolean hasStetho = false;
    try {
      Class.forName(stethoClassName);
      hasStetho = true;
    } catch (ClassNotFoundException ignored) {
    }
    return hasStetho;
  }

  private static boolean hasStethoInterceptorOnClasspath() {
    boolean hasStethoInterceptor = false;
    try {
      Class.forName(stethoInterceptorClassName);
      hasStethoInterceptor = true;
    } catch (ClassNotFoundException ignored) {
    }
    return hasStethoInterceptor;
  }

  /*package*/
  static Interceptor createStethoInterceptor() {

    Constructor StethoInterceptorConstructor;
    Interceptor interceptor = null;

    try {
      Class<?> clazz = Class.forName(stethoInterceptorClassName);
      StethoInterceptorConstructor = clazz.getDeclaredConstructor();
      if (!Modifier.isPublic(StethoInterceptorConstructor.getModifiers())) {
        StethoInterceptorConstructor.setAccessible(true);
      }
      interceptor = (Interceptor) StethoInterceptorConstructor.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return interceptor;
  }
}
