package com.example.common.util;

import android.annotation.SuppressLint;

/**
 * Created by SmartDengg on 2016/3/12.
 */
@SuppressLint("SimpleDateFormat") public class DateUtil {

  public static String coverToDate(String date) {
    //example 20160311
    return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
  }
}
