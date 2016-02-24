package com.example.ultrafit.views.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import butterknife.ButterKnife;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by SmartDengg on 2016/2/25.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

  @Override public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(BaseActivity.this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(BaseActivity.this.getLayoutId());
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
      BaseActivity.this.finish();
    }
    return false;
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(BaseActivity.this);
  }

  protected abstract int getLayoutId();
}
