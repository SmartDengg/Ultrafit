package com.smartdengg.presentation.bitmaps;

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

  protected static final int BLUR_RADIUS = 6;
  protected static final float BLUR_DESATURATE = 0.0f;
  protected static final int BLUR_SCALE = BLUR_RADIUS / 2;

  public static void blurImage(Context context, ImageView target, Bitmap screenSnapshot) {

    BestBlur bestBlur = new BestBlur(context);
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(screenSnapshot, screenSnapshot.getWidth() / BLUR_SCALE,
                                                    screenSnapshot.getHeight() / BLUR_SCALE, true);
    Bitmap blurBitmap = bestBlur.blurBitmap(scaledBitmap, BLUR_RADIUS, BLUR_DESATURATE);
    target.setImageBitmap(blurBitmap);

    if (screenSnapshot != scaledBitmap) scaledBitmap.recycle();
    bestBlur.destroy();
  }

  public static Bitmap retrieveScreenSnapshot(AppCompatActivity activity) {

    View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

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
