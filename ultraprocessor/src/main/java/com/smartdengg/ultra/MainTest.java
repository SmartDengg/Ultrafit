package com.smartdengg.ultra;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建时间:  2017/04/05 11:35 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class MainTest {

  public static void main(String[] args) {

    for (; ; ) ;
  }

  public void writeJSON() {
    JSONObject object = new JSONObject();
    try {
      object.put("name", "Jack Hack");
      object.put("score", Integer.valueOf(200));
      object.put("current", Double.valueOf(152.32));
      object.put("nickname", "Hacker");
    } catch (JSONException ignored) {
    }
  }
}
