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
    "result": [{  }],
    "code": 1,
    "message": ""
  */

  @Expose @SerializedName("code") public Integer resultCode;
  @Expose public String message;

  public Observable filterWebServiceErrors() {
    if (resultCode == Constants.RESULT_OK) {
      return Observable.just(BaseResponse.this);
    } else {
      return Observable.<BaseResponse>error(new WebServiceException(BaseResponse.this.message));
    }
  }
}
