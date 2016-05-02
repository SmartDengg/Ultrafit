package com.smartdengg.model.request;

import com.smartdengg.common.Constants;
import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/24.
 */
@HttpGet(stringUrl = Constants.CITY_URL)
public class CityListRequest extends BaseRequest {

    @Argument(parameter = "     ") private String test1 = "test1";
    @Argument(parameter = "test2") private String test2 = "test2";
    @Argument(parameter = "test3") private String test3 = "test3";
}
