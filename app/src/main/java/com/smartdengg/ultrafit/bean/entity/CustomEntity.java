package com.smartdengg.ultrafit.bean.entity;

/**
 * Created by SmartDengg on 2016/2/14.
 */

public class CustomEntity {
  private String string;

  public CustomEntity(String string) {
    this.string = string;
  }

  @Override public String toString() {
    return "CustomEntity{" +
        "string='" + string + '\'' +
        '}';
  }
}
