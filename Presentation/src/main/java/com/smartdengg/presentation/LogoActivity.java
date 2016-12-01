package com.smartdengg.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.smartdengg.presentation.city.CityActivity;

/**
 * 创建时间:  2016/12/01 11:51 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class LogoActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(null);
    CityActivity.start(LogoActivity.this);
    LogoActivity.this.finish();
  }
}
