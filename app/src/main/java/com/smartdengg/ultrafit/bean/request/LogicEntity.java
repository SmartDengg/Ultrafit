package com.smartdengg.ultrafit.bean.request;

import com.smartdengg.ultrafit.Constant;
import com.smartdengg.ultrafit.bean.entity.CustomEntity;
import com.smartdengg.ultrafit.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.ultrafit.annotation.GET;

/**
 * Created by SmartDengg on 2016/2/14.
 */

@GET(stringUrl = Constant.url) public class LogicEntity {

  @Argument(parameter = "Sting") private String name;
  @Argument(parameter = "Sting[]") private String[] phones;

  @Argument(parameter = "int") private int INT;
  @Argument(parameter = "ints") private int ints;
  @Argument(parameter = "Integer") private Integer[] INTEGER;

  @Argument(parameter = "isboolean") private boolean aBoolean;
  @Argument(parameter = "isBoolean") private Boolean BOOLEAN;

  @Argument(parameter = "CustomEntity1") private CustomEntity customEntity1;
  @Argument(parameter = "CustomEntity2") private CustomEntity customEntity2;

  public LogicEntity(String name, String[] phones, int INT, Integer[] INTEGER, boolean aBoolean, Boolean BOOLEAN,
                     CustomEntity customEntity1, CustomEntity customEntity2) {
    this.name = name;
    this.phones = phones;
    this.INT = INT;
    this.INTEGER = INTEGER;
    this.aBoolean = aBoolean;
    this.BOOLEAN = BOOLEAN;
    this.customEntity1 = customEntity1;
    this.customEntity2 = customEntity2;
  }
}
