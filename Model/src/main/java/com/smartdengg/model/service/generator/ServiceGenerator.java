package com.smartdengg.model.service.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartdengg.common.Constants;
import com.smartdengg.model.BuildConfig;
import com.smartdengg.model.repository.adapter.callAdapter.SmartCallAdapterFactory;
import com.smartdengg.model.repository.adapter.rxadapter.RxJavaCallAdapterFactory;
import com.smartdengg.model.repository.coverter.GsonConverterFactory;
import com.smartdengg.model.repository.interceptor.SmartHttpLoggingInterceptor;
import com.smartdengg.model.service.provider.Injector;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Joker on 2016/2/19.
 */
@SuppressWarnings("all")
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
            ServiceGenerator.httpClientBuilder.addNetworkInterceptor(StethoGenerator.createdStethoInterceptor());
        }

        if (Injector.builder != null) {
            ServiceGenerator.httpClientBuilder = Injector.builder;
        } else {
            ServiceGenerator.httpClientBuilder.addInterceptor(Injector.provideHeaderInterceptor());
        }

        ServiceGenerator.httpClientBuilder.addInterceptor(Injector.provideHttpLoggingInterceptor(SmartHttpLoggingInterceptor.Level.BODY));

        ServiceGenerator.retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                                                          .addCallAdapterFactory(SmartCallAdapterFactory.create())
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
