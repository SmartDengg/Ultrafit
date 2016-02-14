package com.smartdengg.ultrafit.Ultrafit;

/**
 * Created by SmartDengg on 2016/2/14.
 */
public class Utils {

  public static RuntimeException methodError(Class clazz, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    return new IllegalArgumentException(clazz.getSimpleName() + ": " + message);
  }
}
