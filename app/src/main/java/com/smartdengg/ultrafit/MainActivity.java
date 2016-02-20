package com.smartdengg.ultrafit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.smartdengg.ultrafit.bean.entity.CustomEntity;
import com.smartdengg.ultrafit.bean.request.LogicEntity;
import com.smartdengg.ultrafit.ultrafit.RequestEntity;
import com.smartdengg.ultrafit.ultrafit.UltraParser;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String name = "小鄧子";
    String[] phones = { "157", "247", "12330" };

    int i = 1;
    Integer[] integers = { 1, 2, 3, 666 };

    boolean b = true;
    Boolean boo = false;

    LogicEntity logicEntity =
        new LogicEntity(name, phones, i, integers, b, boo, new CustomEntity("1"), new CustomEntity("2"));

    RequestEntity requestEntity = UltraParser.createParser(logicEntity).parseRequestEntity();

    System.out.println("requestEntity = " + requestEntity.toString());
  }
}
