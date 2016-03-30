package com.example.common.rxcompat;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by SmartDengg on 2016/3/30.
 */
public class Rxbus {

  private SerializedSubject<Object, Object> rxBus;
  private SerializedSubject<Object, Object> rxStickBus;

  @SuppressWarnings("unchecked") private Rxbus() {
    this.rxBus = new SerializedSubject(PublishSubject.create());
    this.rxStickBus = new SerializedSubject(BehaviorSubject.create());
  }

  private static class SingletonHolder {
    private static Rxbus instance = new Rxbus();
  }

  public static Rxbus getInstance() {
    return SingletonHolder.instance;
  }

  public void postEvent(Object event) {
    rxBus.onNext(event);
  }

  public void postStickEvent(Object event) {
    rxStickBus.onNext(event);
  }

  public <T> Observable<T> toObservable(Class<T> type) {
    return rxBus.asObservable().ofType(type).onBackpressureBuffer();
  }

  public <T> Observable<T> toStickObservable(Class<T> type) {
    return rxStickBus.asObservable().ofType(type).onBackpressureBuffer();
  }

  public boolean hasObservers() {
    return rxBus.hasObservers();
  }

  public boolean hasStickObservers() {
    return rxStickBus.hasObservers();
  }
}
