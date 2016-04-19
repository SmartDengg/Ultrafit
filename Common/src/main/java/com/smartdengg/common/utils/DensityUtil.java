package com.smartdengg.common.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.View;

public class DensityUtil {

  private static final String TAG = DensityUtil.class.getSimpleName();

  private static int statusBarHeight;
  private static int actionBarSize;

  public static int getLocationY(View item) {

    int[] startingLocation = new int[2];
    /* 得到相对于整个屏幕的区域坐标（左上角坐标——右下角坐标）*/
    Rect viewRect = new Rect();
    item.getGlobalVisibleRect(viewRect);

    startingLocation[0] = viewRect.top + viewRect.bottom;

    return startingLocation[0] / 2 - DensityUtil.getStatusBarHeight(item.getContext()) - DensityUtil.getActionBarSize(
        item.getContext());
  }

  public static int getStatusBarHeight(Context context) {

    if (statusBarHeight == 0) {
      int resourceId =
          context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (resourceId > 0) {
        statusBarHeight = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
      }
    }
    return statusBarHeight;
  }

  public static int getActionBarSize(Context context) {

    if (actionBarSize == 0) {
      TypedArray actionbarSizeTypedArray = null;
      try {
        actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        actionBarSize = (int) actionbarSizeTypedArray.getDimension(0, 0);
      } finally {
        if (actionbarSizeTypedArray != null) {
          actionbarSizeTypedArray.recycle();
        }
      }
    }

    return actionBarSize;
  }

  public static float calculateScale(Rect startBounds, Rect finalBounds) {

    float scale;
    if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
      /* Extend start bounds horizontally*/
      scale = (float) startBounds.height() / finalBounds.height();
      float startWidth = scale * finalBounds.width();
      float deltaWidth = (startWidth - startBounds.width()) / 2;
      startBounds.left -= deltaWidth;
      startBounds.right += deltaWidth;
    } else {
      /* Extend start bounds vertically*/
      scale = (float) startBounds.width() / finalBounds.width();
      float startHeight = scale * finalBounds.height();
      float deltaHeight = (startHeight - startBounds.height()) / 2;
      startBounds.top -= deltaHeight;
      startBounds.bottom += deltaHeight;
    }

    return scale;
  }
}