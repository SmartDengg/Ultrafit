package com.smartdengg.domain.entity;

/**
 * Created by SmartDengg on 2016/2/24.
 */

public class CityEntity implements Cloneable {

  private String cityId;
  private String cityName;

  public CityEntity() {
  }

  public CityEntity newInstance() {

    try {
      return (CityEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }
}
