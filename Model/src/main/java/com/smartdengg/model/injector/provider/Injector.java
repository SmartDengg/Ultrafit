package com.smartdengg.model.injector.provider;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.utils.CacheUtil;
import com.smartdengg.httpservice.lib.interceptor.HttpLoggingInterceptor;
import com.smartdengg.model.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SmartDengg on 2016/6/21.
 */
public class Injector {

  private static int MAXSIZE = 10 * 1024 * 1024;
  private static final String HTTP_CACHE = "HTTP-cache";
  private static final String CACHE_CONTROL = "Cache-Control";

  public static OkHttpClient.Builder builder;

  private Injector() {
    throw new AssertionError("No instance!");
  }

  public static void setupOkHttpBuilder(@NonNull Context context) {
    //noinspection ConstantConditions
    if (context == null) throw new NullPointerException("context == null");

    Context appContext;
    if (context instanceof Application) {
      appContext = context;
    } else {
      appContext = context.getApplicationContext();
    }

    builder = new OkHttpClient.Builder().addInterceptor(provideHeaderInterceptor())
        .addInterceptor(provideOfflineCacheInterceptor(context))
        .addNetworkInterceptor(provideCacheInterceptor())
        .cache(provideApiCache(appContext));
  }

  public static Interceptor provideHeaderInterceptor() {
    return HeaderInterceptor.create();
  }

  private static Interceptor provideCacheInterceptor() {
    return CacheInterceptor.create();
  }

  private static Interceptor provideOfflineCacheInterceptor(Context context) {
    return OfflineCacheInterceptor.create(context);
  }

  private static Cache provideApiCache(Context context) {
    Cache cache = null;
    try {
      cache = new Cache(new File(context.getCacheDir(), HTTP_CACHE), MAXSIZE); // 10 MB
    } catch (Exception e) {
      Logger.t(0).e(e.toString());
    }
    return cache;
  }

  private static boolean hasNetwork(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }

  public static Interceptor provideHttpLoggingInterceptor(HttpLoggingInterceptor.Level level) {
    return HttpLoggingInterceptor.createLoggingInterceptor().setLevel(level);
  }

  public static Cache providePicCache(Context context) {
    File diskCacheDir = CacheUtil.createDiskCacheDir(context);
    return new Cache(diskCacheDir, CacheUtil.calculateDiskCacheSize(diskCacheDir));
  }

  /**
   * Interceptor to add HTTP headers.
   *
   * we also add a custom header "Build-Time" which represent the build time for our module
   */
  private static class HeaderInterceptor implements Interceptor {

    static HeaderInterceptor create() {
      return new HeaderInterceptor();
    }

    @Override public Response intercept(Chain chain) throws IOException {

      Request.Builder builder = chain.request()
          .newBuilder()
          .addHeader("Accept-Encoding", "application/json")
          .addHeader("Build-Time", BuildConfig.BUILD_TIME);

      return chain.proceed(builder.build());
    }
  }

  /**
   * Interceptor to cache data and maintain it for a minute.
   *
   * If the same network request is sent within a minute,
   * the response is retrieved from cache.
   */
  private static class CacheInterceptor implements Interceptor {

    static CacheInterceptor create() {
      return new CacheInterceptor();
    }

    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Response response = chain.proceed(chain.request());
      // re-write response header to force use of cache
      CacheControl cacheControl = new CacheControl.Builder().maxAge(1, TimeUnit.MINUTES).build();
      return response.newBuilder().header(CACHE_CONTROL, cacheControl.toString()).build();
    }
  }

  /**
   * Interceptor to cache data and maintain it for four weeks.
   *
   * If the device is offline, stale (at most four weeks old)
   * response is fetched from the cache.
   */
  private static class OfflineCacheInterceptor implements Interceptor {

    private Context context;

    OfflineCacheInterceptor(Context context) {
      this.context = context;
    }

    static OfflineCacheInterceptor create(Context context) {
      return new OfflineCacheInterceptor(context);
    }

    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      if (!hasNetwork(context)) {
        CacheControl cacheControl = new CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build();
        request = request.newBuilder().cacheControl(cacheControl).build();
      }
      return chain.proceed(request);
    }
  }
}
