package com.smartdengg.ultrafit.ultrafit;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import com.facebook.stetho.Stetho;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.orhanobut.logger.Logger;
import com.smartdengg.ultrafit.service.ServiceGenerator;
import com.squareup.picasso.Picasso;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class MyApplication extends Application {

  private Picasso.Listener picassoListener = new Picasso.Listener() {
    public final String TAG = Picasso.class.getSimpleName();

    @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
      Logger.e(exception.getMessage());
    }
  };

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(MyApplication.this);

    Picasso picasso = new Picasso.Builder(MyApplication.this)
        .downloader(new OkHttp3Downloader(new OkHttpClient()))
        .executor(Executors.newCachedThreadPool())
        .listener(picassoListener)
        .defaultBitmapConfig(Bitmap.Config.ARGB_8888)
        .build();

    Picasso.setSingletonInstance(picasso);

    Logger.init("OkHttp");
    ServiceGenerator.initService();
  }
}
