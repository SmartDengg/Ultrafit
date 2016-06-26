package com.smartdengg.ultra.core;

import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.RestMethod;
import com.smartdengg.ultra.annotation.RestType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory<R> {

    private static final String TAG = UltraParserFactory.class.getSimpleName();
    private static final String HTTP_METHOD = "stringUrl";
    private static final String LOG_FLAG = "LOG";

    private void outputs(RequestEntity requestEntity) {
        Utils.checkNotNull(requestEntity, "requestEntity == null");
        System.out.println(requestEntity.toString());
    }

    R rawRequestEntity;
    RequestEntity requestEntity;
    Class<?> clazz;

    private UltraParserFactory(R rawRequestEntity) {

        if (Modifier.isInterface(rawRequestEntity.getClass().getModifiers())) {
            throw Utils.methodError(this.clazz, "Only class can be parsed,%s is a Interface", rawRequestEntity.getClass().getSimpleName());
        }

        this.rawRequestEntity = rawRequestEntity;
    }

    /** Safe because of generics erasure */
    @SuppressWarnings("unchecked")
    public static <R> UltraParserFactory createParser(R requestEntity) {
        return new UltraParserFactory(Utils.checkNotNull(requestEntity, "requestEntity == null"));
    }

    public RequestEntity parseRequestEntity() {

        UltraParserFactory.this.parseRestUrl();
        UltraParserFactory.this.parseParams();

        if (requestEntity.isShouldOutputs()) UltraParserFactory.this.outputs(requestEntity);

        return requestEntity;
    }

    private void parseRestUrl() {
        RequestEntity tempEntity = UltraParserFactory.this.internalParseUrl();
        requestEntity.setRestType(tempEntity.getRestType()).setUrl(tempEntity.getUrl()).setShouldOutputs(tempEntity.isShouldOutputs());
    }

    void parseParams() {
        RequestEntity tempEntity = UltraParserFactory.this.internalParseParameter();
        requestEntity.setParamMap(tempEntity.getParamMap());
    }

    private RequestEntity internalParseUrl() {

        RestType restType = null;
        String url = null;
        boolean logFlag = true;

        this.clazz = rawRequestEntity.getClass();
        Annotation[] annotations = this.clazz.getAnnotations();

        for (Annotation httpAnnotation : annotations) {

            Class<? extends Annotation> clazz = httpAnnotation.annotationType();
            if (!clazz.isAnnotationPresent(RestMethod.class)) continue;

            RestMethod restMethod = clazz.getAnnotation(RestMethod.class);

            if (restType != null) {
                String previousUrl = Reflections.invokeMethod(httpAnnotation, clazz, HTTP_METHOD);
                throw Utils.methodError(this.clazz, "Only one HTTP method is allowed!\n Found: %s: '%s' or %s: '%s'!", restType, url, restMethod
                        .type(), previousUrl);
            }

            /*Only HttpGet or HttpPost*/
            restType = restMethod.type();
            url = Reflections.invokeMethod(httpAnnotation, clazz, HTTP_METHOD);
            logFlag = Reflections.invokeMethod(httpAnnotation, clazz, LOG_FLAG);
        }

        if (restType == null || url == null) {
            throw Utils.methodError(this.clazz, "Http method annotation is required (e.g.@HttpGet, @HttpPost, etc.).");
        }

        return new RequestEntity(restType, url, null, logFlag);
    }

    private RequestEntity internalParseParameter() {

        if (requestEntity.getRestType() == null || requestEntity.getUrl() == null) {
            throw Utils.methodError(this.clazz, "You should first invoke parseUrl() before call this method.");
        }

        Map<String, String> params = new HashMap<>();
        Class<?> superClazz = this.clazz.getSuperclass();

        while (superClazz != null) {

            if (Object.class.getName().equalsIgnoreCase(superClazz.getName())) {
                break;
            }

            Field[] superFields = superClazz.getDeclaredFields();
            superClazz = superClazz.getSuperclass();

            if (superFields == null || superFields.length == 0) continue;

            UltraParserFactory.this.hunterParams(params, superFields);
        }

        Field[] subFields = this.clazz.getDeclaredFields();
        UltraParserFactory.this.hunterParams(params, subFields);

        return new RequestEntity(null, null, Collections.unmodifiableMap(params), false);
    }

    private void hunterParams(Map<String, String> params, Field[] declaredFields) {
        for (Field field : declaredFields) {

            if (field.isAnnotationPresent(Argument.class)) {

                String name;
                Object value;
                String ultraValue;

                if (!Modifier.isPublic(field.getModifiers())) field.setAccessible(true);

                try {
                    value = field.get(rawRequestEntity);
                } catch (IllegalAccessException e) {
                    throw Utils.methodError(field.getDeclaringClass(),
                            "IllegalAccessException was happened when access " + "%s field", field.getName());
                }

                if (value == null) continue;

                name = field.getAnnotation(Argument.class).parameter();
                ultraValue = Utils.toString(value, Types.getRawType(field.getType()));

                if (params.containsKey(name)) {
                    throw Utils.methodError(field.getDeclaringClass(), "The parameter %s at least already exists one.You must choose one " +
                            "from these which value is '%s'" + " or" + " '%s'", name, params.get(name), ultraValue);
                }

                if (name == null || name.trim().isEmpty()) {
                    name = field.getName();
                }

                params.put(name, ultraValue);
            }
        }
    }
}
