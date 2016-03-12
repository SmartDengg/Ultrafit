package com.example.model.bean.repository;

import com.example.common.Constants;
import com.example.model.bean.repository.adapter.callAdapter.SmartCallAdapterFactory;
import com.example.model.bean.repository.adapter.rxadapter.RxJavaCallAdapterFactory;
import com.example.model.bean.repository.coverter.GsonConverterFactory;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Joker on 2016/2/19.
 */
public class ServiceGenerator {

  private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
  private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

  static {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                 .enableComplexMapKeySerialization()
                                 .serializeNulls()
                                 .create();

    ServiceGenerator.httpClientBuilder.addNetworkInterceptor(new StethoInterceptor())
                                      .addInterceptor(HeaderInterceptor.createdInterceptor())
                                      .addInterceptor(
                                          new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));

    ServiceGenerator.retrofitBuilder.baseUrl(Constants.BASE_URL)
                                    .addCallAdapterFactory(SmartCallAdapterFactory.create())
                                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .client(httpClientBuilder.build());
  }

  public static <S> S createService(Class<S> serviceClass) {
    return retrofitBuilder.build().create(serviceClass);
  }
}
