package com.smartdengg.presentation;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.smartdengg.common.Constants;
import com.smartdengg.common.utils.CacheUtil;
import com.smartdengg.model.service.StethoGenerator;
import com.squareup.picasso.Picasso;
import java.io.File;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MyApplication extends Application {

    private Picasso.Listener picassoListener = new Picasso.Listener() {
        @Override
        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
            Logger.t(0)
                  .e(exception.getMessage());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) AndroidDevMetrics.initWith(MyApplication.this);
        if (BuildConfig.DEBUG && StethoGenerator.HAS_STETHO) StethoGenerator.initializeWithDefaults(MyApplication.this);

        File cacheFile = CacheUtil.createDiskCacheDir(MyApplication.this);
        long cacheSize = CacheUtil.calculateDiskCacheSize(cacheFile);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(new Cache(cacheFile, cacheSize))
                                                              .build();

        Picasso picasso = new Picasso.Builder(MyApplication.this).downloader(new OkHttp3Downloader(okHttpClient))
                                                                 .listener(picassoListener)
                                                                 .defaultBitmapConfig(Bitmap.Config.ARGB_8888)
                                                                 .build();
        Picasso.setSingletonInstance(picasso);

        Logger.init(Constants.BASE_TAG)
              .setMethodOffset(0)
              .setMethodCount(3)
              .setLogLevel(LogLevel.FULL);
    }
}
