package com.example.common;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

/**
 * Created by SmartDengg on 2016/2/25.
 */
public class ExecutorsManager {

  private SynchronizeExecutor synchronizeExecutor;
  private MainThreadExecutor mainThreadExecutor;

  private ExecutorsManager() {
    this.synchronizeExecutor = new SynchronizeExecutor();
    this.mainThreadExecutor = new MainThreadExecutor();
  }

  private static class SingletonHolder {
    private static ExecutorsManager instance = new ExecutorsManager();
  }

  public static ExecutorsManager getInstance() {
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
