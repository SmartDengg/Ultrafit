package com.smartdengg.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SmartDengg on 2016/2/24.
 */
public class CityListResponse {

  /*result": [
      {
        "id": "1",
        "city_name": "上海",
        "city_pre": "S",
        "city_pinyin": "Shanghai",
        "city_short": "sh",
        "count": "161"
      },
    ]*/

  @Expose @SerializedName("id") public String cityId;
  @Expose @SerializedName("city_name") public String cityName;
}
