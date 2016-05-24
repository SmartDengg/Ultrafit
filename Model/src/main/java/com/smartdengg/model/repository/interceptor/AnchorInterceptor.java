package com.smartdengg.model.repository.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Joker on 2016/2/19.
 */
public class AnchorInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private AnchorInterceptor() {
    }

    public static AnchorInterceptor createdInterceptor() {
        return new AnchorInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request()
                               .newBuilder()
                               .addHeader("Github", "com.github.SmartDengg")
                               .build();
        //System.out.println("Request:" + request.toString());

        Response response = chain.proceed(request);
        //System.out.println("Response:" + response.toString());

        ResponseBody responseBody = response.body();
        if (responseBody == null || responseBody.contentLength() == 0 ||
                responseBody.contentLength() > Integer.MAX_VALUE) {
            return response;
        }

        BufferedSource bufferedSource = null;
        Response newResponse;
        try {

            bufferedSource = responseBody.source();
            bufferedSource.request(Long.MAX_VALUE);
            Buffer buffer = bufferedSource.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            System.out.println(buffer.clone()
                                     .readString(charset));

            newResponse = response.newBuilder()
                                  .body(ResponseBody.create(contentType, buffer.size(), buffer.clone()))
                                  .build();
        } finally {
            if (bufferedSource != null) {
                Util.closeQuietly(bufferedSource);
            }
        }

        return newResponse;
    }
}
