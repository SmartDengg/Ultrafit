package com.example.model.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieListResponse  extends BaseResponse{


  /*result": [
        {
            "movieId": "21250",
            "movieName": "白日梦想家",
            "pic_url": "http://img31.mtime.cn/mt/2013/11/26/074429.68199128_96X128.jpg"
        },
       ]*/

  @Expose @SerializedName("result") private List<Result> resultList;

  public List<Result> getResultList() {
    return resultList;
  }

  public class Result {
    @Expose public String movieId;
  }
}
