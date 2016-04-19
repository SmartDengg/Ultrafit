package com.smartdengg.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import java.io.File;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * Created by SmartDengg on 2016/3/12.
 */
public class CacheUtil {

  private static final String ULTRA_CACHE = "ultra-picasso-cache";
  private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
  private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

  private static int calculateMemoryCacheSize(Context context) {
    ActivityManager activityManager = CacheUtil.getService(context, ACTIVITY_SERVICE);
    boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;
    int memoryClass = activityManager.getMemoryClass();
    if (largeHeap && SDK_INT >= HONEYCOMB) {
      memoryClass = activityManager.getLargeMemoryClass();
    }
    // Target ~15% of the available heap.
    return 1024 * 1024 * memoryClass / 7;
  }

  public static File createDiskCacheDir(Context context) {
    File cache = new File(context.getApplicationContext().getCacheDir(), ULTRA_CACHE);
    if (!cache.exists()) {
      cache.mkdirs();
    }
    return cache;
  }

  public static long calculateDiskCacheSize(File dir) {
    long size = MIN_DISK_CACHE_SIZE;

    try {
      StatFs statFs = new StatFs(dir.getAbsolutePath());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        size = statFs.getBlockCountLong() * statFs.getBlockSizeLong() / 50;
      } else {
        size = statFs.getBlockCount() * statFs.getBlockSize() / 50;
      }
    } catch (IllegalArgumentException ignored) {
    }
    return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
  }

  @SuppressWarnings("unchecked")
  static <T> T getService(Context context, String service) {
    return (T) context.getSystemService(service);
  }
}
