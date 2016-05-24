package com.smartdengg.model.request;

import com.smartdengg.common.Constants;
import com.smartdengg.ultra.annotation.Argument;
import com.smartdengg.ultra.annotation.HttpGet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SmartDengg on 2016/2/24.
 */
@HttpGet(stringUrl = Constants.CITY_URL)
public class CityListRequest extends BaseRequest {

   /* @Argument(parameter = "     ") private String test1 = "test1";
    @Argument private String test2 = "test2";
    @Argument(parameter = "test3") private String test3 = "test3";*/

    @Argument(parameter = "test1") private List<String> test1;

    public CityListRequest() {
        test1 = new ArrayList<>();
        test1.add("233333");
    }
}
