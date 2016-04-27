package com.smartdengg.presentation;

import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;

/**
 * Created by SmartDengg on 2016/3/30.
 */
public class Rxbus {

    private static final int initialCount = 1;

    private SerializedSubject<Object, Object> rxBus;
    private SerializedSubject<Object, Object> rxStickBus;

    private HashMap<String, Integer> stickEvents = new HashMap<>();

    @SuppressWarnings("unchecked")
    private Rxbus() {
        this.rxBus = new SerializedSubject(PublishSubject.create());
        this.rxStickBus = new SerializedSubject(ReplaySubject.create());
    }

    private static class SingletonHolder {

        private static Rxbus instance = new Rxbus();
    }

    public static Rxbus getInstance() {
        return SingletonHolder.instance;
    }

    public void postEvent(Object event) {
        if (this.hasObservers()) rxBus.onNext(event);
    }

    public void postStickEvent(Object event) {

        synchronized (Rxbus.this) {

            String name = event.getClass()
                               .getCanonicalName();

            if (stickEvents.containsKey(name)) {
                Integer oldCount = stickEvents.get(name);
                stickEvents.put(name, ++oldCount);
            } else {
                stickEvents.put(name, initialCount);
            }
        }

        rxStickBus.onNext(event);
    }

    public <T> Observable<T> subscribeEvent(Class<T> type) {

        return rxBus.asObservable()
                    .ofType(type)
                    .onBackpressureBuffer();
    }

    public <T> Observable<T> subscribeStickEvent(Class<T> type) {

        return rxStickBus.asObservable()
                         .ofType(type)
                         .buffer(stickEvents.get(type.getCanonicalName()))
                         .map(new Func1<List<T>, T>() {
                             @Override
                             public T call(List<T> ts) {
                                 return ts.get(ts.size());
                             }
                         });
    }

    private boolean hasObservers() {
        return rxBus.hasObservers();
    }
}
