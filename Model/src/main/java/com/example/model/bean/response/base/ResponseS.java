package com.example.model.bean.response.base;

import com.example.common.Constants;
import com.example.common.errors.WebServiceException;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class ResponseS<S> extends BaseResponse<List<S>> {

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

  @Override public Observable<List<S>> filterWebServiceErrors() {
    if (Constants.RESULT_OK.equals(errorCode)) {
      return Observable.just(data);
    } else {
      return Observable.<List<S>>error(new WebServiceException(reason));
    }
  }
}
