package com.smartdengg.ultra.util;

/**
 * Created by Joker on 2016/4/25.
 */
public class Utils {

    public static RuntimeException methodError(Class clazz, String message, Object... args) {
        message = (args.length == 0) ? message : String.format(message, args);
        return new IllegalArgumentException(clazz.getSimpleName() + ": " + message);
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null)
            throw new NullPointerException(message);
        return object;
    }
}
