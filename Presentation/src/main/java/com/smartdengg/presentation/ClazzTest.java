package com.smartdengg.presentation;

import com.google.gson.Gson;

/**
 * 创建时间:  2016/11/09 11:10 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ClazzTest {

  static class Test {

  }

  class God {

  }

  class Parent extends God {
    Parent test() {
      return null;
    }
  }

  class Son extends Parent {
    Son test() {
      return null;
    }
  }

  public static void main(String[] args) throws ClassNotFoundException {

    Class<?> clazz = Class.forName("com.smartdengg.presentation.MainTest");

    String json = "";
    Object pojo = new Gson().fromJson(json, clazz);
    String name = pojo.getClass().getName();

    System.out.printf("name = " + name);
  }
}
