package com.smartdengg.ultra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joker on 2016/4/25.
 */
class Utils {

  static RuntimeException methodError(Class clazz, String message, Object... args) {
    message = (args.length == 0) ? message : String.format(message, args);
    return new IllegalArgumentException(clazz.getSimpleName() + ": " + message);
  }

  static <T> T checkNotNull(T object, String message) {
    if (object == null) throw new NullPointerException(message);
    return object;
  }

  static String toString(Object object, Class<?> rawType) {

    if (!rawType.isArray()) return object.toString();

    Class<?> parameterType = Utils.boxIfPrimitive(rawType.getComponentType());
    String value;
    if (parameterType == Boolean.class) {/* Boolean[] */
      try {
        value = Arrays.toString(((Boolean[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((boolean[]) object));
      }
    } else if (parameterType == Byte.class) {/* Byte[] */
      try {
        value = Arrays.toString(((Byte[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((byte[]) object));
      }
    } else if (parameterType == Character.class) {/* Character[] */
      try {
        value = Arrays.toString(((Character[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((char[]) object));
      }
    } else if (parameterType == Double.class) {/* Double[] */
      try {
        value = Arrays.toString(((Double[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((double[]) object));
      }
    } else if (parameterType == Float.class) {/* Float[] */
      try {
        value = Arrays.toString(((Float[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((float[]) object));
      }
    } else if (parameterType == Integer.class) {/* Integer[] */
      try {
        value = Arrays.toString(((Integer[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((int[]) object));
      }
    } else if (parameterType == Long.class) {/* Long[] */
      try {
        value = Arrays.toString(((Long[]) object));
      } catch (ClassCastException e) {
        value = Arrays.toString(((long[]) object));
      }
    } else if (parameterType == Short.class) {/* Short[] */
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

  /**
   * If the type of this field is a primitive type, the field value is automatically boxed.
   */
  private static Class<?> boxIfPrimitive(Class<?> type) {
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

  private static String toString(Object obj) {
    if (obj == null) {
      return "null";
    }
    if (obj instanceof CharSequence) {
      return '"' + printableToString(obj.toString()) + '"';
    }

    Class<?> cls = obj.getClass();
    if (Byte.class == cls) {
      return byteToString((Byte) obj);
    }

    if (cls.isArray()) {
      return arrayToString(cls.getComponentType(), obj);
    }
    return obj.toString();
  }

  private static String printableToString(String string) {
    int length = string.length();
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; ) {
      int codePoint = string.codePointAt(i);
      switch (Character.getType(codePoint)) {
        case Character.CONTROL:
        case Character.FORMAT:
        case Character.PRIVATE_USE:
        case Character.SURROGATE:
        case Character.UNASSIGNED:
          switch (codePoint) {
            case '\n':
              builder.append("\\n");
              break;
            case '\r':
              builder.append("\\r");
              break;
            case '\t':
              builder.append("\\t");
              break;
            case '\f':
              builder.append("\\f");
              break;
            case '\b':
              builder.append("\\b");
              break;
            default:
              builder.append("\\u").append(String.format("%04x", codePoint).toUpperCase(Locale.US));
              break;
          }
          break;
        default:
          builder.append(Character.toChars(codePoint));
          break;
      }
      i += Character.charCount(codePoint);
    }
    return builder.toString();
  }

  private static String arrayToString(Class<?> cls, Object obj) {
    if (byte.class == cls) {
      return byteArrayToString((byte[]) obj);
    }
    if (short.class == cls) {
      return Arrays.toString((short[]) obj);
    }
    if (char.class == cls) {
      return Arrays.toString((char[]) obj);
    }
    if (int.class == cls) {
      return Arrays.toString((int[]) obj);
    }
    if (long.class == cls) {
      return Arrays.toString((long[]) obj);
    }
    if (float.class == cls) {
      return Arrays.toString((float[]) obj);
    }
    if (double.class == cls) {
      return Arrays.toString((double[]) obj);
    }
    if (boolean.class == cls) {
      return Arrays.toString((boolean[]) obj);
    }
    return arrayToString((Object[]) obj);
  }

  private static String arrayToString(Object[] array) {
    StringBuilder stringBuilder = new StringBuilder();
    arrayToString(array, stringBuilder, new HashSet<Object[]>());
    return stringBuilder.toString();
  }

  private static void arrayToString(Object[] array, StringBuilder builder, Set<Object[]> seen) {
    if (array == null) {
      builder.append("null");
      return;
    }

    seen.add(array);
    builder.append('[');
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        builder.append(", ");
      }

      Object element = array[i];
      if (element == null) {
        builder.append("null");
      } else {
        Class elementClass = element.getClass();
        if (elementClass.isArray() && elementClass.getComponentType() == Object.class) {
          Object[] arrayElement = (Object[]) element;
          if (seen.contains(arrayElement)) {
            builder.append("[...]");
          } else {
            arrayToString(arrayElement, builder, seen);
          }
        } else {
          builder.append(toString(element));
        }
      }
    }
    builder.append(']');
    seen.remove(array);
  }

  private static String byteArrayToString(byte[] bytes) {
    StringBuilder builder = new StringBuilder("[");
    for (int i = 0; i < bytes.length; i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(byteToString(bytes[i]));
    }
    return builder.append(']').toString();
  }

  private static String byteToString(Byte b) {
    if (b == null) {
      return "null";
    }
    return "0x" + String.format("%02x", b).toUpperCase(Locale.US);
  }

  static JSONObject getJsonFromMap(Map<String, ?> map) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    for (String key : map.keySet()) {
      Object value = map.get(key);
      if (value instanceof Map) {
        //noinspection unchecked
        value = getJsonFromMap((Map<String, ?>) value);
      }
      jsonObject.put(key, value);
    }
    return jsonObject;
  }
}
