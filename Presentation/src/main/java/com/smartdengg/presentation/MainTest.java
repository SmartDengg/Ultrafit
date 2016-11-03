package com.smartdengg.presentation;

import com.smartdengg.common.Constants;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建时间:  2016/10/31 15:46 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MainTest {

  static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
  static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
  static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);

  public static void main(String[] args) {

    /*List<String> fixedList = new ArrayList<>(3);
    fixedList.add("1");
    fixedList.add("2");
    fixedList.add("3");

    String[] strings = fixedList.toArray(new String[5]);

    System.out.printf(Arrays.toString(strings));*/

    String s1 = "users/list?sort=desc";
    String s2 = "group/{id}/users";
    String url = Constants.BASE_URL + s1 + '/' + s2;
    //parseHttpMethodAndPath(url);

    A a = (A) createInstance(B.class);
    B b = (B) createInstance(B.class);

    b.getItem();
    // Crash
    a.getItem();
  }

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
