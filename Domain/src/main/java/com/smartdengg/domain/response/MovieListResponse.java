package com.smartdengg.domain.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MovieListResponse {


  /*result": [
        {
            "movieId": "21250",
            "title": "白日梦想家",
            "pic_url": "http://img31.mtime.cn/mt/2013/11/26/074429.68199128_96X128.jpg"
        },
       ]*/

  @Expose public String movieId;
  @Expose public String movieName;
  @SerializedName("pic_url") @Expose public String picUrl;
}
