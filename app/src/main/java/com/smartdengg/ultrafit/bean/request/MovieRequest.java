package com.smartdengg.ultrafit.bean.request;

import com.smartdengg.ultrafit.Constants;
import com.smartdengg.ultrafit.ultrafit.annotation.Argument;
import com.smartdengg.ultrafit.ultrafit.annotation.GET;

/**
 * Created by SmartDengg on 2016/2/21.
 */
@GET(stringUrl = Constants.MOVIE_URL) public class MovieRequest {

  @Argument(parameter = "type") private String type;
  @Argument(parameter = "offset") private int offset;
  @Argument(parameter = "limit") private int limit;

  public MovieRequest(String type, int offset, int limit) {
    this.type = type;
    this.offset = offset;
    this.limit = limit;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }
}
