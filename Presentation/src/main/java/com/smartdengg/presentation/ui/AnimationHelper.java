package com.smartdengg.presentation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import com.smartdengg.common.utils.DensityUtil;

/**
 * Created by SmartDengg on 2016/10/23.
 */
public class AnimationHelper {

  public static void collapseToolbar(Context context, final Toolbar toolbar,
      final AnimationEndListener listener) {

    if (toolbar == null) throw new NullPointerException("toolbar == null");

    Integer shortAnimTime =
        context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    Integer longAnimTime = context.getResources().getInteger(android.R.integer.config_longAnimTime);

    int contentViewHeight = toolbar.getHeight();
    int toolBarHeight = DensityUtil.getActionBarSize(context);
    ValueAnimator valueHeightAnimator = ValueAnimator.ofInt(contentViewHeight, toolBarHeight);
    valueHeightAnimator.setDuration(longAnimTime * 2);
    valueHeightAnimator.setInterpolator(new DecelerateInterpolator(2.0f));
    valueHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        layoutParams.height = (Integer) animation.getAnimatedValue();
        toolbar.setLayoutParams(layoutParams);
      }
    });

    valueHeightAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        if (listener != null) listener.onAnimationEnd(animation);
      }
    });
    valueHeightAnimator.setStartDelay(shortAnimTime);
    valueHeightAnimator.start();
  }

  public interface AnimationEndListener {
    void onAnimationEnd(Animator animation);
  }
}
