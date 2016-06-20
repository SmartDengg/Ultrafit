package com.smartdengg.model.service.provider;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.utils.CacheUtil;
import com.smartdengg.model.repository.interceptor.HeaderInterceptor;
import com.smartdengg.model.repository.interceptor.SmartHttpLoggingInterceptor;
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
        throw new IllegalStateException("No instance!");
    }

    public static void setOkHttpBuilderInstance(Application application) {

        builder = new OkHttpClient.Builder().addInterceptor(provideHeaderInterceptor())
                                            .addInterceptor(provideOfflineCacheInterceptor(application))
                                            .addNetworkInterceptor(provideCacheInterceptor())
                                            .cache(provideCache(application));
    }

    private static Cache provideCache(Application application) {
        Cache cache = null;
        try {
            cache = new Cache(new File(application.getCacheDir(), HTTP_CACHE), MAXSIZE); // 10 MB
        } catch (Exception e) {
            Logger.t(0)
                  .e(e.toString());
        }
        return cache;
    }

    private static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder().maxAge(2, TimeUnit.MINUTES)
                                                                      .build();

                return response.newBuilder()
                               .header(CACHE_CONTROL, cacheControl.toString())
                               .build();
            }
        };
    }

    @SuppressWarnings("all")
    private static Interceptor provideOfflineCacheInterceptor(@NonNull final Application application) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (application != null && !hasNetwork(application)) {
                    CacheControl cacheControl = new CacheControl.Builder().maxStale(7, TimeUnit.DAYS)
                                                                          .build();

                    request = request.newBuilder()
                                     .cacheControl(cacheControl)
                                     .build();
                }

                return chain.proceed(request);
            }
        };
    }

    private static boolean hasNetwork(@NonNull Application application) {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static Interceptor provideHeaderInterceptor() {
        return HeaderInterceptor.createdInterceptor();
    }

    public static Interceptor provideHttpLoggingInterceptor(SmartHttpLoggingInterceptor.Level level) {
        return SmartHttpLoggingInterceptor.createLoggingInterceptor()
                                          .setLevel(level);
    }

    public static Cache providePicCache(File diskPicCacheDir) {
        return new Cache(diskPicCacheDir, CacheUtil.calculateDiskCacheSize(diskPicCacheDir));
    }
}
