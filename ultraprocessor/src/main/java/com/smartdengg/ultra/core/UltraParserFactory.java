package com.smartdengg.ultra.core;

import java.lang.reflect.Modifier;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class UltraParserFactory<R> {

    private static final String TAG = UltraParserFactory.class.getSimpleName();

    R request;

    //@formatter:on
    @SuppressWarnings("unchecked")
    public static <R> UltraParserFactory createParser(R requestEntity) {
        return new UltraParserFactory(Utils.checkNotNull(requestEntity, "requestEntity == null"));
    }

    private UltraParserFactory(R request) {

        //@formatter:off
        if (Modifier.isInterface(Utils.checkNotNull(request, "request == null").getClass().getModifiers())) {
            throw Utils.methodError(request.getClass(), "Only class can be parsed,%s is a Interface", request.getClass().getSimpleName());
        }

        this.request = request;
    }

    public RequestEntity parseRequestEntity() {
        return new RequestBuilder<>(request).build();
    }
}
