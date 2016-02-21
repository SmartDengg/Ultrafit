package com.smartdengg.ultrafit.service;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Joker on 2016/2/19.
 */
public class HeaderInterceptor implements Interceptor {

  private String accessToken;

  public HeaderInterceptor() {
  }

  public HeaderInterceptor(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override public Response intercept(Chain chain) throws IOException {

   /* String token = MyApplication.getInstance().mSharedPreferencesFactory.getToken();
    if (Tools.isEmpty(token)) {
      token = accessToken;
    }

    Request.Builder builder = chain.request()
                                   .newBuilder()
                                   .addHeader("User-Agent", DeviceUtil.getUserAgent())
                                   .addHeader("Accept-Encoding", "gzip, deflate")
                                   .addHeader("Lianjia-App-Id", "lianjia-im");

    if (!Tools.isEmpty(token)) builder.addHeader("Lianjia-access-token", token);*/

    Request.Builder builder = chain.request().newBuilder().addHeader("Accept-Encoding", "application/json");

    return chain.proceed(builder.build());
  }
}
