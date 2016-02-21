package com.smartdengg.ultrafit.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class ProductResponse extends BaseResponse {

  @Expose @SerializedName("result") private List<Product> products;

  public List<Product> getProducts() {
    return products;
  }

  public class Product {

    @Expose @SerializedName("shop_price") public String goodPrice;
    @Expose @SerializedName("goods_name") public String goodName;
    @Expose @SerializedName("original_img") public String goodThumb;

    @Override public String toString() {
      return "Product{" +
          "goodName='" + goodName + '\'' +
          ", goodPrice='" + goodPrice + '\'' +
          ", goodThumb='" + goodThumb + '\'' +
          '}';
    }
  }

  @Override public String toString() {
    return "ProductEntity{" +
        "products=" + products +
        '}';
  }
}
