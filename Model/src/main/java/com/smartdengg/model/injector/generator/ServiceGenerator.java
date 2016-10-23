package com.smartdengg.model.injector.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartdengg.common.Constants;
import com.smartdengg.httpservice.lib.adapter.callAdapter.HttpCallAdapterFactory;
import com.smartdengg.httpservice.lib.adapter.rxadapter.RxJavaCallAdapterFactory;
import com.smartdengg.httpservice.lib.converter.GsonConverterFactory;
import com.smartdengg.httpservice.lib.interceptor.HttpLoggingInterceptor;
import com.smartdengg.model.BuildConfig;
import com.smartdengg.model.injector.provider.Injector;
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

    if (!BuildConfig.RELEASE && StethoGenerator.HAS_STETHO_INTERCEPTOR) {
      ServiceGenerator.httpClientBuilder.addNetworkInterceptor(
          StethoGenerator.createdStethoInterceptor());
    }

    if (Injector.builder != null) {
      ServiceGenerator.httpClientBuilder = Injector.builder;
    } else {
      ServiceGenerator.httpClientBuilder.addInterceptor(Injector.provideHeaderInterceptor());
    }

    ServiceGenerator.httpClientBuilder.addInterceptor(
        Injector.provideHttpLoggingInterceptor(HttpLoggingInterceptor.Level.BODY));

    ServiceGenerator.retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(HttpCallAdapterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(ServiceGenerator.httpClientBuilder.build())
        .validateEagerly(!BuildConfig.RELEASE)
        .build();
  }

  public static <S> S createService(Class<S> serviceClass) {
    return retrofit.create(serviceClass);
  }
}
