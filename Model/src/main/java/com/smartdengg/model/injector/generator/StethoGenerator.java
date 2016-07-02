package com.smartdengg.model.injector.generator;

import android.content.Context;
import com.smartdengg.ultra.core.Reflections;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import okhttp3.Interceptor;

/**
 * Created by SmartDengg on 2016/5/16.
 */
public class StethoGenerator {

    public static final boolean HAS_STETHO = hasStethoOnClasspath();
    public static final boolean HAS_STETHO_INTERCEPTOR = hasStethoInterceptorOnClasspath();

    private static final String stethoClassName = "com.facebook.stetho.Stetho";
    private static final String stethoInterceptorClassName = "com.facebook.stetho.okhttp3.StethoInterceptor";
    private static final String initializeWithDefaults = "initializeWithDefaults";

    private StethoGenerator() {
        throw new IllegalStateException("No instance");
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

    public static void initializeWithDefaults(Context context) {

        try {
            Method initializeWithDefaultsMethod =
                    Reflections.getDeclaredMethod(Class.forName(stethoClassName), initializeWithDefaults, Context.class);
            /**null for static methods*/
            initializeWithDefaultsMethod.invoke(null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Interceptor createdStethoInterceptor() {
        Constructor StethoInterceptorConstructor = Reflections.getConstructor(stethoInterceptorClassName);
        return (Interceptor) Reflections.newInstance(StethoInterceptorConstructor);
    }
}
