package com.smartdengg.model.service;

import android.content.Context;
import com.smartdengg.ultra.util.Reflections;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import okhttp3.Interceptor;

/**
 * Created by SmartDengg on 2016/5/16.
 */
public class StethoPlatform {

    public static final boolean HAS_STETHO = hasStethoOnClasspath();
    public static final boolean HAS_STETHO_INTERCEPTOR = hasStethoInterceptorOnClasspath();

    private StethoPlatform() {
        throw new IllegalStateException("No instance");
    }

    private static boolean hasStethoOnClasspath() {

        boolean hasStetho = false;

        try {
            Class.forName("com.facebook.stetho.Stetho.Stetho");
            hasStetho = true;
        } catch (ClassNotFoundException ignored) {
        }
        return hasStetho;
    }

    private static boolean hasStethoInterceptorOnClasspath() {

        boolean hasStethoInterceptor = false;

        try {
            Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor");
            hasStethoInterceptor = true;
        } catch (ClassNotFoundException ignored) {
        }
        return hasStethoInterceptor;
    }

    public static void initializeWithDefaults(Context context) {

        Constructor stethoConstructor = Reflections.getConstructor("com.facebook.stetho.Stetho.Stetho");
        Object stethoInstance = Reflections.newInstance(stethoConstructor);
        Method initializeWithDefaultsMethod =
                Reflections.getDeclaredMethod(stethoInstance.getClass(), "initializeWithDefaults", Context.class);
        try {
            initializeWithDefaultsMethod.invoke(stethoInstance, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Interceptor createdStethoInterceptor() {
        Constructor StethoInterceptorConstructor = Reflections.getConstructor("com.facebook.stetho.okhttp3.StethoInterceptor");
        return (Interceptor) Reflections.newInstance(StethoInterceptorConstructor);
    }
}
