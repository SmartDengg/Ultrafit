package com.smartdengg.domain.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SmartDengg on 2016/2/24.
 */
public class CityResponse {

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

  @Expose public String id;
  @Expose @SerializedName("city_name") public String cityName;
  @Expose @SerializedName("city_pre") public String cityPre;
  @Expose @SerializedName("city_pinyin") public String cityPinyin;
  @Expose @SerializedName("city_short") public String cityShort;
}
