package com.example.model.bean.request;

import com.example.common.Constants;
import com.smartdengg.ultrafit.annotation.HttpGet;

/**
 * Created by SmartDengg on 2016/2/24.
 */
@HttpGet(stringUrl = Constants.CITY_URL, LOG = false)
public class CityListRequest extends BaseRequest {}
