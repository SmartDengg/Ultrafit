package com.smartdengg.ultra.core;

import com.smartdengg.ultra.*;
import java.lang.reflect.Constructor;

/**
 * Created by SmartDengg on 2016/4/24.
 */
class RxJavaGenerator {

    @SuppressWarnings("unchecked")
    static <T> T createdObservable(RequestEntity requestEntity) {

        if (!Platform.HAS_RX_OBSERVABLE) return null;

        /** Observable.just("requestEntity");*/

        Constructor scalarSynchronousObservableConstructor =
                Reflections.getConstructor("rx.internal.util.ScalarSynchronousObservable", Object.class);

        return (T) Reflections.newInstance(scalarSynchronousObservableConstructor, requestEntity);
    }

    @SuppressWarnings("unchecked")
    static <T> T createdSingle(RequestEntity requestEntity) {

        if (!Platform.HAS_RX_SINGLE) return null;

        /** Single.just("requestEntity");*/

        Constructor scalarSynchronousSingleConstructor =
                Reflections.getConstructor("rx.internal.util.ScalarSynchronousSingle", Object.class);

        return (T) Reflections.newInstance(scalarSynchronousSingleConstructor, requestEntity);
    }
}
