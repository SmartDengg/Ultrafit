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
import com.smartdengg.httpservice.lib.HttpService;
import com.smartdengg.model.injector.generator.StethoGenerator;
import com.smartdengg.model.injector.provider.Injector;
import com.squareup.picasso.Picasso;
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

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder().cache(Injector.providePicCache(CacheUtil.createDiskCacheDir(MyApplication.this)))
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

        Injector.setOkHttpBuilderInstance(MyApplication.this);

        HttpService.setHttpTAG("LOG-HTTP")
                   .enableResponseLog(true);
    }
}
