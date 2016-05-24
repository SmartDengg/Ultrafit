package com.smartdengg.ultra.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by SmartDengg on 2016/4/24.
 */
public class Reflections {

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
            if (!Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
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

    public static Object invokeMethod(Method method, Object instance, Object... parameters) {

        Object returnObject = null;

        try {
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            returnObject = method.invoke(instance, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnObject;
    }
}
