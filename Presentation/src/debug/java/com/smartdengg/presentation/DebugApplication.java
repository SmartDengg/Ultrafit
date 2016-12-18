package com.smartdengg.presentation;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class DebugApplication extends ReleaseApplication {

  @Override public void onCreate() {
    super.onCreate();
    AndroidDevMetrics.initWith(DebugApplication.this);
    Stetho.initializeWithDefaults(DebugApplication.this);
    OomExceptionHandler.install(DebugApplication.this);

    if (LeakCanary.isInAnalyzerProcess(this)) return;
    LeakCanary.install(this);
  }
}
