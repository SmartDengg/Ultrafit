package com.example.common.repository;

import android.util.Log;
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

  /**
   * Android's max limit for a log entry is ~4076 bytes,
   * so 4000 bytes is used as chunk size since default charset
   * is UTF-8
   */
  private static SmartHttpLoggingInterceptor httpLoggingInterceptor =
      new SmartHttpLoggingInterceptor(new SmartHttpLoggingInterceptor.Logger() {
        @Override public void log(String message) {
          for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
              int end = Math.min(newline, i + Constants.MAX_LOG_LENGTH);
              Log.d(Constants.BASE_TAG + "-" + Constants.OKHTTP_TAG,
                    Constants.HORIZONTAL_DOUBLE_LINE + message.substring(i, end));
              i = end;
            } while (i < newline);
          }
        }

        @Override public void logTopBorder() {
          Log.d(Constants.BASE_TAG + "-" + Constants.OKHTTP_TAG, Constants.TOP_BORDER);
        }

        @Override public void logMiddleBorder() {
          Log.d(Constants.BASE_TAG + "-" + Constants.OKHTTP_TAG, Constants.MIDDLE_BORDER);
        }

        @Override public void logBottomBorder() {
          Log.d(Constants.BASE_TAG + "-" + Constants.OKHTTP_TAG, Constants.BOTTOM_BORDER);
        }
      });

  static {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .create();

    ServiceGenerator.httpClientBuilder
        .addNetworkInterceptor(new StethoInterceptor())
        .addInterceptor(HeaderInterceptor.createdInterceptor())
        .addInterceptor(httpLoggingInterceptor.setLevel(SmartHttpLoggingInterceptor.Level.HEADERS));

    retrofit = new Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
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
