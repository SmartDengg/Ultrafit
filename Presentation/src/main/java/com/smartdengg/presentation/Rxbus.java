package com.smartdengg.presentation;

import java.util.List;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;

/**
 * Created by SmartDengg on 2016/3/30.
 */
public class Rxbus {

    private SerializedSubject<Object, Object> rxBus;
    private SerializedSubject<Object, Object> rxStickBus;

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
        rxStickBus.onNext(event);
    }

    public <T> void subscribeEvent(Class<T> type, Action1<T> action) {
        rxBus.asObservable()
             .ofType(type)
             .onBackpressureBuffer()
             .subscribe(action);
    }

    public <T> void subscribeStickEvent(Class<T> type, Action1<T> action) {
        rxStickBus.asObservable()
                  .ofType(type)
                  .buffer(Integer.MAX_VALUE)
                  .map(new Func1<List<T>, T>() {
                      @Override
                      public T call(List<T> ts) {
                          return ts.get(ts.size());
                      }
                  })
                  .subscribe(action);
    }

    private boolean hasObservers() {
        return rxBus.hasObservers();
    }
}
