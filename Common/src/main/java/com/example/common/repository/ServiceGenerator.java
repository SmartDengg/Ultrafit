package com.example.common.repository;

import com.example.common.Constants;
import com.example.common.repository.adapter.callAdapter.SmartCallAdapterFactory;
import com.example.common.repository.adapter.rxadapter.RxJavaCallAdapterFactory;
import com.example.common.repository.coverter.GsonConverterFactory;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Joker on 2016/2/19.
 */
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private static Retrofit retrofit;

    static {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .enableComplexMapKeySerialization()
                                     .serializeNulls()
                                     .setPrettyPrinting()
                                     .create();

        ServiceGenerator.httpClientBuilder.addNetworkInterceptor(new StethoInterceptor())
                                          .addInterceptor(HeaderInterceptor.createdInterceptor())
                                          .addInterceptor(SmartHttpLoggingInterceptor.createLoggingInterceptor()
                                                                                     .setLevel(SmartHttpLoggingInterceptor.Level.HEADERS));

        retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                                         .addCallAdapterFactory(SmartCallAdapterFactory.create())
                                         .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                         .addConverterFactory(GsonConverterFactory.create(gson))
                                         .client(httpClientBuilder.build())
                                         .validateEagerly(true)
                                         .build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
