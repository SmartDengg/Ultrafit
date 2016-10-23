package com.smartdengg.presentation.views.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by SmartDengg on 2016/2/25.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

  private Unbinder bind;

  @Override public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    this.bind = ButterKnife.bind(BaseActivity.this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(BaseActivity.this.getLayoutId());
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        BaseActivity.this.exit();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
      BaseActivity.this.exit();
    }
    return false;
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (this.bind != null) bind.unbind();
  }

  protected abstract void exit();

  @LayoutRes protected abstract int getLayoutId();
}
