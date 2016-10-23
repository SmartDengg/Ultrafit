package com.smartdengg.presentation.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joker on 2016/1/3.
 */
public class SmartTextView extends TextView {

  private static Map<String, Typeface> typefaceMap = new HashMap<>();

  public SmartTextView(Context context) {
    this(context, null);
  }

  public SmartTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SmartTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    if (!isInEditMode()) {
      SmartTextView.this.init(context);
    }
  }

  private void init(Context context) {
    Typeface typeface;

    if ((typeface = typefaceMap.get("Lobster-Regular.ttf")) == null) {
      typefaceMap.put("Lobster-Regular.ttf",
          typeface = Typeface.createFromAsset(context.getAssets(), "Lobster-Regular.ttf"));
    }
    this.setTypeface(typeface);
  }
}
