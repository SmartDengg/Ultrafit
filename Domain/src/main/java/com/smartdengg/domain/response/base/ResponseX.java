package com.smartdengg.domain.response.base;

import com.smartdengg.domain.errors.WebServiceException;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class ResponseX<X> extends BaseResponse<X> {

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

  @Override public Observable<X> filterWebServiceErrors() {
    if (RESULT_OK.equals(errorCode) && null != data) {
      return Observable.just(data);
    } else {
      return Observable.error(new WebServiceException(reason));
    }
  }
}
