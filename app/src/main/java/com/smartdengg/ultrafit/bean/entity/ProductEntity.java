package com.smartdengg.ultrafit.bean.entity;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class ProductEntity implements Cloneable {

  private String goodPrice;
  private String goodName;
  private String goodThumbUrl;

  public ProductEntity newInstance() {
    try {
      return (ProductEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getGoodPrice() {
    return goodPrice;
  }

  public void setGoodPrice(String goodPrice) {
    this.goodPrice = goodPrice;
  }

  public String getGoodName() {
    return goodName;
  }

  public void setGoodName(String goodName) {
    this.goodName = goodName;
  }

  public String getGoodThumbUrl() {
    return goodThumbUrl;
  }

  public void setGoodThumbUrl(String goodThumbUrl) {
    this.goodThumbUrl = goodThumbUrl;
  }

  @Override public String toString() {
    return "ProductEntity{" +
        "goodPrice='" + goodPrice + '\'' +
        ", goodName='" + goodName + '\'' +
        ", goodThumbUrl='" + goodThumbUrl + '\'' +
        '}';
  }
}
