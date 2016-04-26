package com.smartdengg.ultra.util;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class Utils {

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static RuntimeException methodError(Class clazz, String message, Object... args) {
        message = (args.length == 0) ? message : String.format(message, args);
        return new IllegalArgumentException(clazz.getSimpleName() + ": " + message);
    }
}
