package com.smartdengg.common;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/**
 * Created by SmartDengg on 2016/2/25.
 */
public class Executors {

  private SynchronizeExecutor synchronizeExecutor;
  private MainThreadExecutor mainThreadExecutor;

  private Executors() {
    this.synchronizeExecutor = new SynchronizeExecutor();
    this.mainThreadExecutor = new MainThreadExecutor();
  }

  private static class SingletonHolder {
    private static Executors instance = new Executors();
  }

  public static Executors getInstance() {
    return SingletonHolder.instance;
  }

  public SynchronizeExecutor synchronizeExecutor() {
    return synchronizeExecutor;
  }

  public MainThreadExecutor mainThreadExecutor() {
    return mainThreadExecutor;
  }

  public class SynchronizeExecutor implements Executor {
    @Override public void execute(@NonNull Runnable command) {
      command.run();
    }
  }

  public class MainThreadExecutor implements Executor {
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override public void execute(@NonNull Runnable command) {
      mainHandler.post(command);
    }
  }
}
