package com.smartdengg.domain.response.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/3/20.
 */
abstract class BaseResponse<T> {

  static final Integer RESULT_OK = 0;

  @Expose protected String reason;
  @Expose @SerializedName("error_code") protected Integer errorCode;
  @Expose @SerializedName("result") protected T data;

  public abstract Observable<T> filterWebServiceErrors();
}
