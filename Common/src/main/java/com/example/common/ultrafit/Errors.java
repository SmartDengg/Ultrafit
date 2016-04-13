package com.example.common.ultrafit;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class Errors {

    public static RuntimeException methodError(Class clazz, String message, Object... args) {
        message = (args.length == 0) ? message : String.format(message, args);
        return new IllegalArgumentException(clazz.getSimpleName() + ": " + message);
    }
}
