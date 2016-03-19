package com.example.model.bean.response.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/3/20.
 */
abstract class BaseResponse<T> {

  @Expose protected String reason;
  @Expose @SerializedName("error_code") protected Integer errorCode;
  @Expose @SerializedName("result") protected T data;

  public abstract Observable<T> filterWebServiceErrors();
}
