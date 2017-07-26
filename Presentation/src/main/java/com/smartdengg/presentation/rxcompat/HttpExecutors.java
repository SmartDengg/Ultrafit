/*
 * Copyright 2016 Lianjia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.smartdengg.presentation.rxcompat;

import android.os.Process;
import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * 创建时间: 2016/08/09 18:36 <br>
 * 作者: dengwei <br>
 * 描述: 用于网络连接的自定义线程池
 */
public class HttpExecutors {

  /*链表结构的缓冲队列长度为128的线程池*/
  public static ExecutorService HttpExecutor;
  /*使用优先任务队列的线程池,需要注意的执行任务的Runnable需要实现{@link java.lang.Comparable}接口以实现优先级*/
  public static ExecutorService HttpPriorityExecutor;
  private static final int DEVICE_INFO_UNKNOWN = 0;
  private static final int CPU_COUNT = HttpExecutors.getCountOfCPU();
  private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
  private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
  private static final int KEEP_ALIVE = 60;
  private static final BlockingQueue<Runnable> sHttpLinkedWaitQueue =
      new LinkedBlockingDeque<>(128);
  private static final BlockingQueue<Runnable> sHttpPriorityWaitQueue =
      new PriorityBlockingQueue<>(128);

  private static final ThreadFactory sHttpThreadFactory = new ThreadFactory() {
    private final AtomicInteger count = new AtomicInteger(1);

    public Thread newThread(@SuppressWarnings("NullableProblems") Runnable runnable) {
      ThreadWrapper threadWrapper =
          new ThreadWrapper(runnable, "HttpExecutor #" + count.getAndIncrement());
      threadWrapper.setDaemon(false);
      return threadWrapper;
    }
  };

  private static final RejectedExecutionHandler rejectedHandler =
      new ThreadPoolExecutor.DiscardOldestPolicy();

  static {
    HttpExecutor =
        new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sHttpLinkedWaitQueue, sHttpThreadFactory, rejectedHandler);
    HttpPriorityExecutor =
        new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sHttpPriorityWaitQueue, sHttpThreadFactory, rejectedHandler);
  }

  private HttpExecutors() {
    throw new AssertionError("No instance");
  }

  /**
   * Linux中的设备都是以文件的形式存在，CPU也不例外，因此CPU的文件个数就等价与核数。
   * Android的CPU 设备文件位于/sys/devices/system/cpu/目录，文件名的的格式为cpu\d+。
   */
  private static int getCountOfCPU() {

    int count;
    try {
      count = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
    } catch (SecurityException | NullPointerException e) {
      count = DEVICE_INFO_UNKNOWN;
    }
    return count;
  }

  private static final FileFilter CPU_FILTER = new FileFilter() {
    @Override public boolean accept(File pathname) {

      String path = pathname.getName();
      if (path.startsWith("cpu")) {
        for (int i = 3; i < path.length(); i++) {
          if (path.charAt(i) < '0' || path.charAt(i) > '9') {
            return false;
          }
        }
        return true;
      }
      return false;
    }
  };

  private static class ThreadWrapper extends Thread {

    ThreadWrapper(Runnable runnable, String threadName) {
      super(runnable, threadName);
    }

    @Override public void run() {
      Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
      super.run();
    }
  }
}
