package com.smartdengg.ultrafit.bean.entity;

/**
 * Created by SmartDengg on 2016/2/14.
 */

public class TestEntity {
  private String string;

  public TestEntity(String string) {
    this.string = string;
  }

  @Override public String toString() {
    return "TestEntity{" +
        "string='" + string + '\'' +
        '}';
  }
}
