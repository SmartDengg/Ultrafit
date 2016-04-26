package com.smartdengg.ultra.util;

import com.smartdengg.ultra.Platform;
import com.smartdengg.ultra.RequestEntity;
import java.lang.reflect.Constructor;

/**
 * Created by SmartDengg on 2016/4/24.
 */
public class RxJavaFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createdObservable(RequestEntity requestEntity) {

        if (!Platform.HAS_RX_OBSERVABLE) return null;

        /** Observable.just("requestEntity");*/

        Constructor scalarSynchronousObservableConstructor =
                Reflections.getConstructor("rx.internal.util.ScalarSynchronousObservable", Object.class);

        //Method createMethod = Reflections.getDeclaredMethod(scalarSynchronousObservable.getClass(), "create", Object.class);
        //return (T) Reflections.invokeMethod(createMethod, scalarSynchronousObservable, requestEntity);

        return (T) Reflections.newInstance(scalarSynchronousObservableConstructor, requestEntity);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createdSingle(RequestEntity requestEntity) {

        if (!Platform.HAS_RX_SINGLE) return null;

        /** Single.just("requestEntity");*/

        Constructor scalarSynchronousSingleConstructor =
                Reflections.getConstructor("rx.internal.util.ScalarSynchronousSingle", Object.class);

        return (T) Reflections.newInstance(scalarSynchronousSingleConstructor, requestEntity);
    }
}
