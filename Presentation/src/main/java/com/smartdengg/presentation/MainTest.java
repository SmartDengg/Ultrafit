package com.smartdengg.presentation;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by SmartDengg on 2016/10/29.
 */
public class MainTest {

  private static boolean stop = false;

  static int item = 1;

  static int value = 1;

  static int delay = 0;

  public static void main(String[] args) throws InterruptedException {

    //cache();

    //asyncSubject();

    //BehaviorSubject();

    //Schedulers.start();

    //threadTest();

    //showBackpressure();

    //request();

    //delay();

    final ScheduledExecutorService mainExecutorService =
        Executors.newScheduledThreadPool(1, new ThreadFactory() {
          @Override public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Main thread");
            thread.setDaemon(true);
            return thread;
          }
        });

    //for (int i = 0, n = 10; i <= n; i++) {
    //final int finalI = i;

    mainExecutorService.scheduleWithFixedDelay(new Runnable() {
      @Override public void run() {

        System.out.println("UpdateView");

        Observable.create(new Observable.OnSubscribe<Integer>() {
          @Override public void call(Subscriber<? super Integer> subscriber) {

            try {
              int t = new Random().nextInt(10) * 1000;
              Thread.sleep(t);
              subscriber.onNext(t);
              subscriber.onCompleted();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        })
            .concatMap(new Func1<Integer, Observable<Integer>>() {
              @Override public Observable<Integer> call(Integer integer) {
                return Observable.just(integer);
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.from(mainExecutorService))
            .subscribe(new Subscriber<Integer>() {
              @Override public void onCompleted() {
                System.out.println("MainTest.onCompleted");
                System.out.println("=================================");
              }

              @Override public void onError(Throwable e) {
                e.printStackTrace();
              }

              @Override public void onNext(final Integer integer) {

                mainExecutorService.execute(new Runnable() {
                  @Override public void run() {
                    System.out.println(
                        "integer = [" + integer + "]" + " Thread = " + Thread.currentThread()
                            .getName());
                  }
                });
              }
            });
      }
    }, 3, 3, TimeUnit.SECONDS);
    //}

    for (; ; ) ;
  }

  private static void delay() {
    Observable.fromAsync(new Action1<AsyncEmitter<Integer>>() {
      @Override public void call(AsyncEmitter<Integer> asyncEmitter) {
        System.out.println("Emit");

        asyncEmitter.onNext(1);
        asyncEmitter.onCompleted();
      }
    }, AsyncEmitter.BackpressureMode.DROP)
        .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
          @Override public Observable<?> call(Observable<? extends Void> observable) {

            return observable.map(new Func1<Void, Integer>() {
              @Override public Integer call(Void aVoid) {
                return getDelay();
              }
            }).concatMap(new Func1<Integer, Observable<Long>>() {
              @Override public Observable<Long> call(Integer integer) {
                return Observable.timer(integer, TimeUnit.SECONDS);
              }
            });
          }
        })
        .subscribe(new Subscriber<Integer>() {
          @Override public void onCompleted() {
            System.out.println("MainTest.onCompleted");
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(Integer integer) {
            System.out.println("integer = " + integer);
          }
        });
  }

  private static int getDelay() {
    System.out.println("delay = " + delay);
    return delay++;
  }

  abstract class Parent {

    abstract Object test(CharSequence s);

    protected Object test1(String s) throws IOException {
      return null;
    }

    Object over(String s) {
      return null;
    }
  }

  class Sun extends Parent {

    @Override String test(CharSequence s) {
      return null;
    }

    @Override protected String test1(String s) throws IOException {
      return null;
    }

    private void test(CharSequence s, String s1) throws IOException {
    }

    @Override Object over(String s) {
      return null;
    }

    Object over(CharSequence s) {
      return null;
    }
  }

  private static void request() {
    Observable.range(1, 10).map(new Func1<Integer, Integer>() {
      @Override public Integer call(Integer integer) {

        return null;
      }
    }).subscribe(new Subscriber<Integer>() {

      String name;

      @Override public void onStart() {

        System.out.println("before request");

        request(1);

        name = "RangeExample";
        System.out.println("after request");
      }

      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(Integer v) {

        if (name == null) {
          System.out.println(" v =" + v);
        } else {
          System.out.println("len = " + name.length() + " v = " + v);
        }

        request(1);
      }
    });
  }

  private static void showBackpressure() {
    PublishSubject<Integer> source = PublishSubject.create();
    source.observeOn(Schedulers.computation()).subscribe(new Action1<Integer>() {
      @Override public void call(Integer v) {
        try {
          Thread.sleep(1_000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(v);
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        throwable.printStackTrace();
      }
    });

    for (int i = 0; i < 1_000_000; i++) {
      source.onNext(i);
    }
  }

  private static void threadTest() throws InterruptedException {

    new Thread(new Runnable() {
      @Override public void run() {
        while (!stop) {
          try {
            System.out.println("call");
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();

    Thread.sleep(1000);
    System.out.println("sleep");

    new Thread(new Runnable() {
      @Override public void run() {
        stop = true;
      }
    }).start();
  }

  public static class ThreadWrapper extends Thread {
    private boolean flag = false;

    public void run() {
      while (!flag) {
        try {
          System.out.println("call");
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public void close() {
      flag = true;
    }
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
    }).cache().onTerminateDetach();

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
    /**
     * The constant TAG.
     */
    public static final String TAG = "";

    /**
     * Instantiates a new Inner.
     */
    public Inner() {
    }

    private void print() {
      System.out.println(TAG);
    }
  }

  private interface Callback {
    /**
     * On call.
     */
    void onCall();
  }

  private abstract class Listener implements Callback {
    /**
     * On observer.
     */
    abstract void onObserver();

    @Override public final void onCall() {

      onObserver();
    }
  }

  private class ListnerImpl extends Listener {
    @Override void onObserver() {

    }
  }

  /**
   * The Param.
   */
  static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
  /**
   * The Param url regex.
   */
  static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
  /**
   * The Param name regex.
   */
  static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);

  private static void parseHttpMethodAndPath(String value) {

    if (value.isEmpty()) return;

    System.out.println("url= " + value);

    // Get the relative URL path and existing query string, if present.
    int question = value.indexOf('?');
    if (question != -1 && question < value.length() - 1) {
      // Ensure the query string does not have any named parameters.
      String queryParams = value.substring(question + 1);

      System.out.println("queryParams = " + queryParams);

      Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(queryParams);
      if (queryParamMatcher.find()) {
        throw new IllegalStateException(String.format(
            "URL query string \"%s\" must not have replace block. "
                + "For dynamic query parameters use @Query.", queryParams));
      }
    }

    Set<String> relativeUrlParamNames = parsePathParameters(value);
    System.out.println("relativeUrlParamNames = " + relativeUrlParamNames);
  }

  /**
   * Parse path parameters set.
   *
   * @param path the path
   * @return the set
   */
  static Set<String> parsePathParameters(String path) {
    Matcher m = PARAM_URL_REGEX.matcher(path);
    Set<String> patterns = new LinkedHashSet<>();
    while (m.find()) {
      patterns.add(m.group(1));
    }
    return patterns;
  }

  /**
   * The interface A.
   */
  interface A {
    /**
     * Gets item.
     *
     * @return the item
     */
    Object getItem();
  }

  /**
   * The interface B.
   */
  interface B extends A {
    String getItem();
  }

  private static final InvocationHandler stubHandler = new InvocationHandler() {
    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return null;
    }
  };

  private static Object createInstance(Class<?> clazz) {
    return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, stubHandler);
  }
}
