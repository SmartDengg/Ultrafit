package com.smartdengg.model.repository.interceptor;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Joker on 2016/2/19.
 */
public class HeaderInterceptor implements Interceptor {

  private HeaderInterceptor() {
  }

  public static HeaderInterceptor createdInterceptor() {
    return new HeaderInterceptor();
  }

  @Override
  public Response intercept(Chain chain) throws IOException {

    Request.Builder builder = chain.request().newBuilder().addHeader("Accept-Encoding", "application/json");

    return chain.proceed(builder.build());
  }
}
