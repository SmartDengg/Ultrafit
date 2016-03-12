package com.example.common.Util;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import java.io.File;

/**
 * Created by SmartDengg on 2016/3/12.
 */
public class FileUtil {

  private static final String ULTRA_CACHE = "ultra-cache";
  private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
  private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

  public static File createCacheDir(Context context) {
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
}
