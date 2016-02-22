package com.example.common.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by SmartDengg on 2016/2/22.
 */
public class BitmapUtil {

  protected static final int BLUR_RADIUS = 10;
  protected static final float BLUR_SCALE = 0.0f;

  public static void blurImage(Context context, ImageView imageView, Bitmap bitmap) {

    BestBlur bestBlur = new BestBlur(context);
    Bitmap blurBitmap = bestBlur.blurBitmap(bitmap, BLUR_RADIUS, BLUR_SCALE);
    imageView.setImageBitmap(blurBitmap);
    bestBlur.destroy();
  }

  public static Bitmap retrieveScreen(AppCompatActivity activity) {

    View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

    rootView.destroyDrawingCache();
    rootView.setDrawingCacheEnabled(true);
    Bitmap drawingCache = rootView.getDrawingCache();

    Bitmap screenBitmap =
        Bitmap.createBitmap(drawingCache.getWidth(), drawingCache.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(screenBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setFlags(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(drawingCache, 0, 0, paint);

    rootView.destroyDrawingCache();

    return screenBitmap;
  }
}
