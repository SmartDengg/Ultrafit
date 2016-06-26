package com.smartdengg.ultra.core;

import java.util.Arrays;

/**
 * Created by Joker on 2016/4/25.
 */
public class Utils {

    static RuntimeException methodError(Class clazz, String message, Object... args) {
        message = (args.length == 0) ? message : String.format(message, args);
        return new IllegalArgumentException(clazz.getSimpleName() + ": " + message);
    }

    static <T> T checkNotNull(T object, String message) {
        if (object == null) throw new NullPointerException(message);
        return object;
    }

    /**
     * If the type of this field is a primitive type, the field value is automatically boxed.
     *
     * @param type
     * @return
     */
    static Class<?> boxIfPrimitive(Class<?> type) {
        if (boolean.class == type) return Boolean.class;
        if (byte.class == type) return Byte.class;
        if (char.class == type) return Character.class;
        if (double.class == type) return Double.class;
        if (float.class == type) return Float.class;
        if (int.class == type) return Integer.class;
        if (long.class == type) return Long.class;
        if (short.class == type) return Short.class;
        return type;
    }

    static String toString(Object object, Class<?> rawType) {

        if (!rawType.isArray()) return object.toString();

        Class<?> parameterType = Utils.boxIfPrimitive(rawType.getComponentType());
        String value;
        if (parameterType == Boolean.class) {/** Boolean[] */
            try {
                value = Arrays.toString(((Boolean[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((boolean[]) object));
            }
        } else if (parameterType == Byte.class) {/** Byte[] */
            try {
                value = Arrays.toString(((Byte[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((byte[]) object));
            }
        } else if (parameterType == Character.class) {/** Character[] */
            try {
                value = Arrays.toString(((Character[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((char[]) object));
            }
        } else if (parameterType == Double.class) {/** Double[] */
            try {
                value = Arrays.toString(((Double[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((double[]) object));
            }
        } else if (parameterType == Float.class) {/** Float[] */
            try {
                value = Arrays.toString(((Float[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((float[]) object));
            }
        } else if (parameterType == Integer.class) {/** Integer[] */
            try {
                value = Arrays.toString(((Integer[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((int[]) object));
            }
        } else if (parameterType == Long.class) {/** Long[] */
            try {
                value = Arrays.toString(((Long[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((long[]) object));
            }
        } else if (parameterType == Short.class) {/** Short[] */
            try {
                value = Arrays.toString(((Short[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((short[]) object));
            }
        } else {
            value = object != null ? Arrays.toString((Object[]) object) : null;
        }

        return value;
    }

}
