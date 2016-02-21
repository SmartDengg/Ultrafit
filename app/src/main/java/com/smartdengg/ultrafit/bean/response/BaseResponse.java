package com.smartdengg.ultrafit.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.smartdengg.ultrafit.Constants;
import com.smartdengg.ultrafit.repository.WebServiceException;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/14.
 */
public class BaseResponse {

  /*
    "data": [{  }],
    "status": 1,
    "control": {"expires": 1800},
  */

  @Expose @SerializedName("status") public Integer status;

  public Observable filterWebServiceErrors() {
    if (status == Constants.RESULT_OK) {
      return Observable.just(BaseResponse.this);
    } else {
      return Observable.<BaseResponse>error(new WebServiceException("Error occur when fetch service"));
    }
  }
}
