package com.smartdengg.presentation.views.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.BindView;
import com.smartdengg.common.Constants;
import com.smartdengg.common.utils.DensityUtil;
import com.smartdengg.common.utils.TypeVerify;
import com.smartdengg.domain.entity.CityEntity;
import com.smartdengg.presentation.AnimationHelper;
import com.smartdengg.presentation.R;
import com.smartdengg.presentation.adapter.CityListAdapter;
import com.smartdengg.presentation.presenter.CityListPresenter;
import com.smartdengg.presentation.presenter.CityListPresenterImp;
import com.smartdengg.presentation.ui.MarginDecoration;
import com.smartdengg.presentation.views.ViewInterface;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/24.
 */
public class CityActivity extends BaseActivity implements ViewInterface<List<CityEntity>> {

  @NonNull @BindString(R.string.city_title) protected String title;

  @NonNull @BindView(R.id.city_layout_root_view) protected ViewGroup rootView;
  @NonNull @BindView(R.id.city_layout_toolbar) protected Toolbar toolbar;
  @NonNull @BindView(R.id.city_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @BindView(R.id.city_layout_rv) protected RecyclerView recyclerView;

  private ActionBar actionBar;
  private CityListAdapter cityListAdapter = new CityListAdapter(CityActivity.this);
  private CityListPresenter<List<CityEntity>> cityListPresenter;

  private CityListAdapter.Callback callback = new CityListAdapter.Callback() {
    @Override public void onItemClick(View itemView, CityEntity cityEntity) {
      int location = DensityUtil.getLocationY(itemView);
      MovieActivity.startFromLocation(CityActivity.this, location, cityEntity.getCityId());
    }

    @Override public void onError(Throwable error) {
      CityActivity.this.closeRefresh();
    }
  };

  private SwipeRefreshLayout.OnRefreshListener listener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override public void onRefresh() {
          CityActivity.this.initData();
        }
      };

  // component type is a parameteriazed type
  public List<String>[] listArrayDemo() {
    return null;
  }

  public <F> F[] arrayF() {
    return null;
  }

  public List<? extends String>[] arrayWhat() {
    return null;
  }

  public static void showGenericArrayType() {
    for (Method method : CityActivity.class.getDeclaredMethods()) {
      System.out.println("############################################");
      System.out.println("Method name : " + method.getName());
      Type type = method.getGenericReturnType();

      //
      TypeVerify.verifyGenericArrayType(type);

      //
      TypeVerify.verifyParameterizedType(type);

      //
      TypeVerify.verifyClass(type);

      //
      TypeVerify.verifyArray(type);

      System.out.println("############################################");
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(null);

    //System.out.println("~0 = " + ~0);

    //showGenericArrayType();

    CityActivity.this.initPresenter();
    CityActivity.this.initView(savedInstanceState);
  }

  @Override protected int getLayoutId() {
    return R.layout.city_activity_layout;
  }

  private void initPresenter() {
    this.cityListPresenter = CityListPresenterImp.created();
    this.cityListPresenter.attachView(CityActivity.this);
  }

  private void initView(Bundle savedInstanceState) {

    CityActivity.this.setSupportActionBar(toolbar);
    actionBar = CityActivity.this.getSupportActionBar();
    if (actionBar != null) actionBar.setTitle(null);

    this.swipeRefreshLayout.setColorSchemeColors(Constants.colors);
    this.swipeRefreshLayout.setOnRefreshListener(listener);
    this.swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        swipeRefreshLayout.setRefreshing(true);
      }
    });

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CityActivity.this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    linearLayoutManager.setSmoothScrollbarEnabled(true);

    this.cityListAdapter.setCallback(callback);
    this.recyclerView.setLayoutManager(linearLayoutManager);
    this.recyclerView.setHasFixedSize(true);
    this.recyclerView.addItemDecoration(new MarginDecoration(CityActivity.this));
    this.recyclerView.setAdapter(cityListAdapter);

    if (savedInstanceState == null) {
      ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
      if (viewTreeObserver.isAlive()) {
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            rootView.getViewTreeObserver().removeOnPreDrawListener(this);
            CityActivity.this.collapseToolbar();
            return true;
          }
        });
      }
    } else {
      ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
      layoutParams.height = DensityUtil.getActionBarSize(CityActivity.this);
      toolbar.setLayoutParams(layoutParams);
      CityActivity.this.initData();
    }
  }

  private void collapseToolbar() {
    AnimationHelper.collapseToolbar(CityActivity.this, toolbar,
        new AnimationHelper.AnimationEndListener() {
          @Override public void onAnimationEnd(Animator animation) {
            if (actionBar != null) actionBar.setTitle(title);
            CityActivity.this.initData();
          }
        });
  }

  private void initData() {
    this.cityListPresenter.loadData();
  }

  @Override public void showData(Observable<List<CityEntity>> data) {
    CityActivity.this.closeRefresh();
    data.subscribe(this.cityListAdapter);
  }

  @Override public void showError(String errorMessage) {
    CityActivity.this.closeRefresh();
    if (errorMessage != null) {
      Toast.makeText(CityActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
  }

  @Override protected void exit() {
    CityActivity.this.finish();
    overridePendingTransition(0, android.R.anim.fade_out);
  }

  private void closeRefresh() {
    if (swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    this.cityListPresenter.detachView();
  }
}
