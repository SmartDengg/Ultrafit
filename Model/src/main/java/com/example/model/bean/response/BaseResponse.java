package com.example.model.bean.response;

import android.annotation.TargetApi;
import android.os.Build;
import com.example.common.Constants;
import com.example.model.bean.WebServiceException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class BaseResponse {

  /*{
      "reason": "成功的返回",
      "result": {},
      "error_code": 0
    }*/

  /*{
      "reason": "当前可请求的次数不足",
      "result": null,
      "error_code": 10012
    }*/

  @Expose String reason;
  @Expose @SerializedName("error_code") Integer errorCode;

  @TargetApi(Build.VERSION_CODES.KITKAT) public Observable filterWebServiceErrors() {
    if (Objects.equals(errorCode, Constants.RESULT_OK)) {
      return Observable.just(BaseResponse.this);
    } else {
      return Observable.<BaseResponse>error(new WebServiceException(reason));
    }
  }
}
