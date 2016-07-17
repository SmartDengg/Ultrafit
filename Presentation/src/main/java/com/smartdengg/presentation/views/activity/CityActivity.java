package com.smartdengg.presentation.views.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.BindString;
import com.smartdengg.common.Constants;
import com.smartdengg.common.utils.DensityUtil;
import com.smartdengg.common.utils.Types;
import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.model.request.CityListRequest;
import com.smartdengg.model.response.CityListResponse;
import com.smartdengg.presentation.R;
import com.smartdengg.presentation.adapter.CityListAdapter;
import com.smartdengg.presentation.presenter.CityListPresenter;
import com.smartdengg.presentation.presenter.CityListPresenterImp;
import com.smartdengg.presentation.ui.MarginDecoration;
import com.smartdengg.presentation.views.ViewInterface;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/24.
 */
public class CityActivity<T extends Observable<CityListResponse>, S extends Observable<CityListResponse>>
    extends BaseActivity implements ViewInterface<List<CityEntity>> {

  @NonNull @BindString(R.string.city_title) protected String title;

  @NonNull @Bind(R.id.city_layout_root_view) protected ViewGroup rootView;
  @NonNull @Bind(R.id.city_layout_toolbar) protected Toolbar toolbar;
  @NonNull @Bind(R.id.city_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @Bind(R.id.city_layout_rv) protected RecyclerView recyclerView;

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

  private List<? extends Observable<CityListRequest>> list1;
  private List<T> list2;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(null);

    try {
      Field fieldA = CityActivity.class.getDeclaredField("list1");
      Field fieldB = CityActivity.class.getDeclaredField("list2");
      Class<?> rawType1 = Types.getRawType(fieldA.getGenericType());
      Class<?> rawType2 = Types.getRawType(fieldB.getGenericType());
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }

    try {
      Method testGeneric = CityActivity.class.getDeclaredMethod("testGeneric");
      Type genericReturnType = testGeneric.getGenericReturnType();
      Class<?> rawType = Types.getRawType(genericReturnType);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    TypeVariable<Class<CityActivity>>[] typeParameters = CityActivity.class.getTypeParameters();
    for (TypeVariable<Class<CityActivity>> typeVariable : typeParameters) {
      Class<?> rawType = Types.getRawType(typeVariable);
    }

    CityActivity.this.initPresenter();
    CityActivity.this.initView(savedInstanceState);
  }

  private <D extends Observable<CityEntity> & Serializable> List testGeneric() {
    return null;
  }

  @Override protected int getLayoutId() {
    return R.layout.city_activity_layout;
  }

  private void initPresenter() {
    this.cityListPresenter = CityListPresenterImp.createdPresenter();
    this.cityListPresenter.attachView(CityActivity.this);
  }

  private void initView(Bundle savedInstanceState) {

    CityActivity.this.setSupportActionBar(toolbar);
    actionBar = CityActivity.this.getSupportActionBar();
    if (actionBar == null) return;
    actionBar.setTitle(null);

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

  @SuppressWarnings("unchecked") private void collapseToolbar() {

    Integer shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
    Integer longAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

    int contentViewHeight = toolbar.getHeight();
    int toolBarHeight = DensityUtil.getActionBarSize(CityActivity.this);
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

        if (actionBar == null) return;
        actionBar.setTitle(title);
        CityActivity.this.initData();
      }
    });
    valueHeightAnimator.setStartDelay(shortAnimTime);
    valueHeightAnimator.start();
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
