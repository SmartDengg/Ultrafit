package com.smartdengg.ultrafit;

import android.support.annotation.NonNull;
import android.util.Log;
import com.smartdengg.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.annotation.RestMethod;
import com.smartdengg.ultrafit.type.RestType;
import com.smartdengg.ultrafit.type.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory<R> {

    private static final String TAG = UltraParserFactory.class.getSimpleName();
    private static final String HttpMethod = "stringUrl";
    private static final String LogEntity = "LOG";

    private void outputs(@NonNull RequestEntity requestEntity) {
        Log.d(TAG, requestEntity.toString());
    }

    private R rawEntity;
    private RequestEntity requestEntity;
    private Class<?> clazz;

    private UltraParserFactory(R rawEntity) {

        if (Modifier.isInterface(rawEntity.getClass()
                                          .getModifiers())) {
            throw Errors.methodError(this.clazz, "Only class can be parsed,%s is a Interface", rawEntity.getClass()
                                                                                                        .getSimpleName());
        }

        this.rawEntity = rawEntity;
        this.requestEntity = new RequestEntity();
    }

    /** Safe because of generics erasure */
    @SuppressWarnings("unchecked")
    public static <R> UltraParserFactory createParser(R requestEntity) {
        return new UltraParserFactory(requestEntity);
    }

    public String parseUrl() {
        UltraParserFactory.this.parseRestUrl();
        return requestEntity.getUrl();
    }

    private void parseRestUrl() {
        RequestEntity tempEntity = UltraParserFactory.this.internalParseUrl();
        requestEntity.setRestType(tempEntity.getRestType())
                     .setUrl(tempEntity.getUrl())
                     .setShouldOutputs(tempEntity.isShouldOutputs());
    }

    public Map<String, String> parseParameter() {
        UltraParserFactory.this.parseParams();
        return requestEntity.getParamMap();
    }

    private void parseParams() {
        RequestEntity tempEntity = UltraParserFactory.this.internalParseParameter();
        requestEntity.setParamMap(tempEntity.getParamMap());
    }

    public RequestEntity parseRequestEntity() {

        if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
            UltraParserFactory.this.parseRestUrl();
        }

        if (requestEntity.getParamMap() == null) {
            UltraParserFactory.this.parseParams();
        }

        if (requestEntity.isShouldOutputs()) UltraParserFactory.this.outputs(requestEntity);

        return requestEntity;
    }

    @NonNull
    private RequestEntity internalParseUrl() {

        RestType restType = null;
        String url = null;
        boolean logEntity = true;

        this.clazz = rawEntity.getClass();
        Annotation[] annotations = this.clazz.getAnnotations();

        for (Annotation classAnnotation : annotations) {

            Class<? extends Annotation> clazz = classAnnotation.annotationType();
            if (!clazz.isAnnotationPresent(RestMethod.class)) {
                continue;
            }

            RestMethod restMethod = clazz.getAnnotation(RestMethod.class);

            if (restType != null) {
                String excessUrl = (String) UltraParserFactory.this.invokeUrl(classAnnotation, clazz, HttpMethod);
                throw Errors.methodError(this.clazz, "Only one HTTP method is allowed!\n Found: %s: '%s' or %s: '%s'!", restType.name(), url, restMethod.type(), excessUrl);
            }

            /*Only HttpGet or HttpPost*/
            restType = restMethod.type();

            url = (String) UltraParserFactory.this.invokeUrl(classAnnotation, clazz, HttpMethod);

            logEntity = (boolean) UltraParserFactory.this.invokeUrl(classAnnotation, clazz, LogEntity);
        }

        if (restType == null || url == null) {
            throw Errors.methodError(this.clazz, "Http method annotation is required (e.g.@HttpGet, @HttpPost, etc.).");
        }

        return new RequestEntity(restType, url, null, logEntity);
    }

    private Object invokeUrl(Annotation classAnnotation, Class<? extends Annotation> clazz, String mehtodName) {
        try {
            return clazz.getMethod(mehtodName)
                        .invoke(classAnnotation);
        } catch (Exception ignore) {
            throw Errors.methodError(this.clazz, "Failed to extract String 'value' from @%s annotation.", clazz.getSimpleName());
        }
    }

    public RequestEntity internalParseParameter() {

        if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
            throw Errors.methodError(this.clazz, "You should first invoke parseUrl() before call this method.");
        }

        Map<String, String> params = new HashMap<>();
        Class<?> superClazz = this.clazz.getSuperclass();

        while (superClazz != null) {

            if (Object.class.getName()
                            .equalsIgnoreCase(superClazz.getName())) {
                break;
            }

            Field[] superFields = superClazz.getDeclaredFields();
            superClazz = superClazz.getSuperclass();

            if (superFields == null || superFields.length == 0) {
                continue;
            }
            UltraParserFactory.this.hunter(params, superFields);
        }

        Field[] subFields = this.clazz.getDeclaredFields();
        UltraParserFactory.this.hunter(params, subFields);

        return new RequestEntity(null, null, Collections.unmodifiableMap(params), false);
    }

    private void hunter(Map<String, String> params, Field[] declaredFields) {
        for (Field field : declaredFields) {

            if (field.isAnnotationPresent(Argument.class)) {

                if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);

                Argument argument = field.getAnnotation(Argument.class);
                Class<?> parameterType = field.getType();
                Class<?> rawParameterType = Types.getRawType(parameterType);

                String name;
                Object value;
                String ultra;

                try {
                    value = field.get(rawEntity);
                } catch (IllegalAccessException e) {
                    throw Errors.methodError(field.getDeclaringClass(),
                            "IllegalAccessException was happened when access " + "%s field", field.getName());
                }

                if (value == null) continue;

                if (rawParameterType.isArray()) {
                    Class<?> arrayComponentType = UltraParserFactory.this.boxIfPrimitive(rawParameterType.getComponentType());
                    name = argument.parameter();
                    ultra = UltraParserFactory.this.arrayToString(value, arrayComponentType);
                } else {
                    name = argument.parameter();
                    ultra = value.toString();
                }

                if (params.containsKey(name)) {
                    throw Errors.methodError(field.getDeclaringClass(),
                            "The parameter %s at least already exists one.You must choose one " +
                                    "from these which value is '%s'" + " or" + " '%s'", name, params.get(name), ultra);
                }
                params.put(name, ultra);
            }
        }
    }

    /**
     * If the type of this field is a primitive type, the field value is automatically boxed.
     *
     * @param type
     * @return
     */
    private Class<?> boxIfPrimitive(Class<?> type) {
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

    private String arrayToString(Object object, Class<?> rawParameterType) {

        String value;

        if (rawParameterType == Boolean.class) {/** Boolean[] */
            try {
                value = Arrays.toString(((Boolean[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((boolean[]) object));
            }
        } else if (rawParameterType == Byte.class) {/** Byte[] */
            try {
                value = Arrays.toString(((Byte[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((byte[]) object));
            }
        } else if (rawParameterType == Character.class) {/** Character[] */
            try {
                value = Arrays.toString(((Character[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((char[]) object));
            }
        } else if (rawParameterType == Double.class) {/** Double[] */
            try {
                value = Arrays.toString(((Double[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((double[]) object));
            }
        } else if (rawParameterType == Float.class) {/** Float[] */
            try {
                value = Arrays.toString(((Float[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((float[]) object));
            }
        } else if (rawParameterType == Integer.class) {/** Integer[] */
            try {
                value = Arrays.toString(((Integer[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((int[]) object));
            }
        } else if (rawParameterType == Long.class) {/** Long[] */
            try {
                value = Arrays.toString(((Long[]) object));
            } catch (ClassCastException e) {
                value = Arrays.toString(((long[]) object));
            }
        } else if (rawParameterType == Short.class) {/** Short[] */
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
