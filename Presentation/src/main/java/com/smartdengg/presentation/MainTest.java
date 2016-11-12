package com.smartdengg.presentation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  public static void main(String[] args) throws InterruptedException {

    /*List<String> fixedList = new ArrayList<>(3);
    fixedList.add("1");
    fixedList.add("2");
    fixedList.add("3");

    String[] strings = fixedList.toArray(new String[5]);

    System.out.printf(Arrays.toString(strings));*/

    /*String s1 = "users/list?sort=desc";
    String s2 = "group/{id}/users";
    String url = Constants.BASE_URL + s1 + '/' + s2;
    //parseHttpMethodAndPath(url);

    A a = (A) createInstance(B.class);
    B b = (B) createInstance(B.class);

    b.getItem();
    // Crash
    a.getItem();*/

    //cache();

    //asyncSubject();

    //BehaviorSubject();

    //Schedulers.start();

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

  static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
  static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
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

  static Set<String> parsePathParameters(String path) {
    Matcher m = PARAM_URL_REGEX.matcher(path);
    Set<String> patterns = new LinkedHashSet<>();
    while (m.find()) {
      patterns.add(m.group(1));
    }
    return patterns;
  }

  interface A {
    Object getItem();
  }

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
