package com.smartdengg.presentation;

import android.content.Context;
import android.os.Debug;
import java.io.File;

/**
 * 创建时间:  2016/11/03 15:57 <br>
 * 作者:  SmartDengg <br>
 * 描述:  Dump the heap on OutOfMemoryError crashes in your debug builds.
 * https://gist.github.com/pyricau/4726389fd64f3b7c6f32
 */
class OomExceptionHandler implements Thread.UncaughtExceptionHandler {
  private static final String FILENAME = "out-of-memory.hprof";

  private final Thread.UncaughtExceptionHandler defaultHandler;
  private final Context context;

  static void install(Context context) {
    Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    if (defaultHandler instanceof OomExceptionHandler) return;

    OomExceptionHandler oomHandler = new OomExceptionHandler(defaultHandler, context);
    Thread.setDefaultUncaughtExceptionHandler(oomHandler);
  }

  private OomExceptionHandler(Thread.UncaughtExceptionHandler defaultHandler, Context context) {
    this.defaultHandler = defaultHandler;
    this.context = context.getApplicationContext();
  }

  @Override public void uncaughtException(Thread thread, Throwable ex) {
    if (containsOom(ex)) {
      File heapDumpFile = new File(context.getFilesDir(), FILENAME);
      try {
        Debug.dumpHprofData(heapDumpFile.getAbsolutePath());
      } catch (Throwable ignored) {
      }
    }
    defaultHandler.uncaughtException(thread, ex);
  }

  private boolean containsOom(Throwable ex) {
    if (ex instanceof OutOfMemoryError) return true;

    while ((ex = ex.getCause()) != null) {
      if (ex instanceof OutOfMemoryError) return true;
    }
    return false;
  }
}
