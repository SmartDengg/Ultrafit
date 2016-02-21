package com.smartdengg.ultrafit.service;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartdengg.ultrafit.Constants;
import com.smartdengg.ultrafit.coverter.GsonConverterFactory;
import com.smartdengg.ultrafit.service.adapter.callAdapter.SmartCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Joker on 2016/2/19.
 */
public class ServiceGenerator {

  private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
  private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

  public static void initService() {

    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
                                 .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
                                 .serializeNulls().create();

    ServiceGenerator.httpClientBuilder
        .addNetworkInterceptor((Constants.isDebugChrome) ? new StethoInterceptor() : null)
        .addInterceptor(new HeaderInterceptor())
        .addInterceptor(
            (Constants.isDebugEnv) ? new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) : null);

    ServiceGenerator.retrofitBuilder
        .baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(SmartCallAdapterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClientBuilder.build());
  }

  public static <S> S createService(Class<S> serviceClass) {

    return retrofitBuilder.build().create(serviceClass);
  }

  public static <S> S createService(Class<S> serviceClass, Gson gson) {

    return retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson)).build().create(serviceClass);
  }
}
