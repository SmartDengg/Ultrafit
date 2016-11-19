package com.smartdengg.presentation;

/**
 * 创建时间:  2016/11/19 00:38 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class VolatileTest extends Thread {
  private static boolean flag = false;

  public void run() {
    while (!flag) {
      System.out.println("VolatileTest.run");
    }
  }

  public static void main(String[] args) throws Exception {
    new VolatileTest().start();
    Thread.sleep(1000);
    flag = true;
  }
}
