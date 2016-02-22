package com.example.model.bean;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class WebServiceException extends RuntimeException {

  public WebServiceException(String detailMessage) {
    super(detailMessage);
  }
}
