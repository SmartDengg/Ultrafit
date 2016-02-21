package com.smartdengg.ultrafit.bean.request;

import com.smartdengg.ultrafit.Constants;
import com.smartdengg.ultrafit.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.ultrafit.annotation.POST;

/**
 * Created by SmartDengg on 2016/2/21.
 */
@POST(stringUrl = Constants.LOGIC_URL) public class ProductRequest {

  @Argument(parameter = "Action") private String action;

  @Argument(parameter = "page") private int pageIndex;

  @Argument(parameter = "count") private int count;

  public ProductRequest(String action, int pageIndex, int count) {
    this.action = action;
    this.pageIndex = pageIndex;
    this.count = count;
  }

  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }
}
