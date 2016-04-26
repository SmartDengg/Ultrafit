package com.smartdengg.common;

import android.graphics.Color;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class Constants {

    public static final String BASE_URL = "http://v.juhe.cn/";

    public static final String CITY_URL = "movie/citys";
    public static final String MOVIE_TODAY_URL = "movie/movies.today";
    public static final String MOVIE_DETAIL_URL = "movie/query";

    public static final String APP_KEY = "e0b8426d4f107de733aed080e7ee65f8";

    public static final String BASE_TAG = "LOG";
    public static final String OKHTTP_TAG = "OkHttp";
    public static final String ULTRA_TAG = "Ultra";
    public static final int MAX_LOG_LENGTH = 4 * 1000;
    public static final Integer RESULT_OK = 0;
    public static final Integer MAX_CONNECT = 4;

    public static boolean isDebugChrome = true;
    public static boolean isDebugJsonLog = true;

    public static int[] colors = new int[] {
            Color.parseColor("#70AF41"), Color.parseColor("#42A5F5"), Color.parseColor("#42A5F5")
    };
}
