package com.example.common.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;

public class DensityUtil {

  private static final String TAG = DensityUtil.class.getSimpleName();

  private static int actionBarSize;

  /**
   * 获取ActionBarSize
   */
  public static int getActionBarSize(Context context) {

    if (actionBarSize == 0) {
      TypedArray actionbarSizeTypedArray = null;
      try {
        actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        actionBarSize = (int) actionbarSizeTypedArray.getDimension(0, 0);
      } finally {
        actionbarSizeTypedArray.recycle();
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