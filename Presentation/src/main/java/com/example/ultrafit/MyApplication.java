package com.example.ultrafit;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import com.example.common.Constants;
import com.example.common.Util.FileUtil;
import com.facebook.stetho.Stetho;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import java.io.File;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class MyApplication extends Application {

  private Picasso.Listener picassoListener = new Picasso.Listener() {
    @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
      Logger.e(exception.getMessage());
    }
  };

  @Override public void onCreate() {
    super.onCreate();

    if (Constants.isDebugChrome) {
      Stetho.initializeWithDefaults(MyApplication.this);
    }

    File cacheFile = FileUtil.createDiskCacheDir(MyApplication.this);
    long cacheSize = FileUtil.calculateDiskCacheSize(cacheFile);
    OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(new Cache(cacheFile, cacheSize)).build();

    Picasso picasso = new Picasso.Builder(MyApplication.this)
        .downloader(new OkHttp3Downloader(okHttpClient))
        .listener(picassoListener)
        .defaultBitmapConfig(Bitmap.Config.ARGB_8888)
        .build();

    Picasso.setSingletonInstance(picasso);

    Logger.init(Constants.LOGGER_TAG).setMethodOffset(0).setMethodCount(4).setLogLevel(LogLevel.FULL);
  }
}
