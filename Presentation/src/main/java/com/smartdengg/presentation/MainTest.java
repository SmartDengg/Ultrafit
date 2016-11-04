package com.smartdengg.presentation;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;

/**
 * Created by SmartDengg on 2016/10/29.
 */
public class MainTest {

  static int item = 1;

  public static void main(String[] args) {

    //cache();

    //asyncSubject();

    BehaviorSubject();

    for (; ; ) ;
  }

  private static void BehaviorSubject() {
    Observable<Long> cold = Observable.interval(1000, TimeUnit.MILLISECONDS);

    BehaviorSubject<Long> behaviorSubject = BehaviorSubject.create(-1L);

    cold.subscribe(behaviorSubject);

    addSomeDelay();

    behaviorSubject.subscribe(subscriber1);
    behaviorSubject.subscribe(subscriber2);

    addSomeDelay();
  }

  private static void asyncSubject() {
    Observable<Long> cold = Observable.create(new Observable.OnSubscribe<Long>() {
      @Override public void call(Subscriber<? super Long> subscriber) {
        for (long i = 0; i <= 2; i++) {
          System.out.println("Source Emits : " + i);
          subscriber.onNext(i);
        }
        subscriber.onCompleted();
      }
    });

    AsyncSubject<Long> asyncSubject = AsyncSubject.create();
    cold.subscribe(asyncSubject);

    addSomeDelay();

    asyncSubject.subscribe(subscriber1);
    asyncSubject.subscribe(subscriber2);
  }

  private static Action1<Long> subscriber1 = new Action1<Long>() {
    @Override public void call(Long aLong) {
      System.out.println("Subscriber 1 :" + aLong);
    }
  };

  private static Action1<Long> subscriber2 = new Action1<Long>() {
    @Override public void call(Long aLong) {
      System.out.println("Subscriber 2 :" + aLong);
    }
  };

  private static void cache() {
    Observable<Integer> cache = Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {

        subscriber.onNext(item);
        subscriber.onCompleted();
      }
    }).cache();

    cache.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println(integer);
      }
    });

    item = 2;

    cache.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println(integer);
      }
    });
  }

  private static void addSomeDelay() {
    try {
      System.out.println("Wait for some seconds");
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private class Inner {
    public static final String TAG = "";

    public Inner() {
    }

    private void print() {
      System.out.println(TAG);
    }
  }

  private interface Callback {
    void onCall();
  }

  private abstract class Listener implements Callback {
    abstract void onObserver();

    @Override public final void onCall() {

      onObserver();
    }
  }

  private class ListnerImpl extends Listener {
    @Override void onObserver() {

    }
  }
}
