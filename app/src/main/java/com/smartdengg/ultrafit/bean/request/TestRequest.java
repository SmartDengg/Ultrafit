package com.smartdengg.ultrafit.bean.request;

import com.smartdengg.ultrafit.Constants;
import com.smartdengg.ultrafit.bean.entity.TestEntity;
import com.smartdengg.ultrafit.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.ultrafit.annotation.GET;

/**
 * Created by SmartDengg on 2016/2/14.
 */

@GET(stringUrl = Constants.MOVIE_URL) public class TestRequest {

  @Argument(parameter = "Sting") private String name;
  @Argument(parameter = "Sting[]") private String[] phones;

  @Argument(parameter = "int") private int INT;
  @Argument(parameter = "ints") private int ints;
  @Argument(parameter = "Integer") private Integer[] INTEGER;

  @Argument(parameter = "isboolean") private boolean aBoolean;
  @Argument(parameter = "isBoolean") private Boolean BOOLEAN;

  @Argument(parameter = "CustomEntity1") private TestEntity testEntity1;
  @Argument(parameter = "CustomEntity2") private TestEntity testEntity2;

  public TestRequest(String name, String[] phones, int INT, Integer[] INTEGER, boolean aBoolean, Boolean BOOLEAN,
                     TestEntity testEntity1, TestEntity testEntity2) {
    this.name = name;
    this.phones = phones;
    this.INT = INT;
    this.INTEGER = INTEGER;
    this.aBoolean = aBoolean;
    this.BOOLEAN = BOOLEAN;
    this.testEntity1 = testEntity1;
    this.testEntity2 = testEntity2;
  }
}
