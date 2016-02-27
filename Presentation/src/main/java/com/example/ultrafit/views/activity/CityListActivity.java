package com.example.ultrafit.views.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.BindString;
import com.example.common.Constants;
import com.example.common.Util.DensityUtil;
import com.example.model.bean.entity.CityEntity;
import com.example.ultrafit.R;
import com.example.ultrafit.adapter.CityListAdapter;
import com.example.ultrafit.presenter.CityListPresenter;
import com.example.ultrafit.presenter.CityListPresenterImp;
import com.example.ultrafit.ui.MarginDecoration;
import com.example.ultrafit.views.ListView;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/24.
 */
public class CityListActivity extends BaseActivity implements ListView<CityEntity> {

  @NonNull @BindString(R.string.city_title) protected String title;

  @NonNull @Bind(R.id.city_layout_root_view) protected ViewGroup rootView;

  @NonNull @Bind(R.id.city_layout_title_tv) protected TextView titleTv;
  @NonNull @Bind(R.id.city_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @Bind(R.id.city_layout_rv) protected RecyclerView recyclerView;

  private CityListAdapter cityListAdapter;
  private CityListPresenter<CityEntity> cityListPresenter;

  private int contentViewHeight;

  private CityListAdapter.Callback callback = new CityListAdapter.Callback() {
    @Override public void onItemClick(View itemView, CityEntity cityEntity) {

      int location = DensityUtil.getLocationY(itemView);
      MovieListActivity.startFromLocation(CityListActivity.this, location, cityEntity.getCityId());
      overridePendingTransition(0, 0);
    }

    @Override public void onError(Throwable error) {
      CityListActivity.this.closeRefresh();
    }
  };

  private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override public void onRefresh() {
      CityListActivity.this.initData();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(null);

    CityListActivity.this.initView(savedInstanceState);

    this.cityListPresenter = CityListPresenterImp.createdPresenter();
    this.cityListPresenter.attachView(CityListActivity.this);
  }

  @Override protected int getLayoutId() {
    return R.layout.city_activity;
  }

  private void initView(Bundle savedInstanceState) {

    swipeRefreshLayout.setColorSchemeResources(Constants.colors);
    swipeRefreshLayout.setOnRefreshListener(listener);
    swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        swipeRefreshLayout.setRefreshing(true);
      }
    });

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CityListActivity.this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    linearLayoutManager.setSmoothScrollbarEnabled(false);

    recyclerView.addItemDecoration(new MarginDecoration(CityListActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(linearLayoutManager);
    cityListAdapter = new CityListAdapter(CityListActivity.this);
    cityListAdapter.setCallback(callback);
    recyclerView.setAdapter(cityListAdapter);

    if (savedInstanceState == null) {

      ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
      if (viewTreeObserver.isAlive()) {
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            rootView.getViewTreeObserver().removeOnPreDrawListener(this);

            CityListActivity.this.contentViewHeight = titleTv.getHeight();
            CityListActivity.this.collapseToolbar();
            return true;
          }
        });
      }
    } else {
      CityListActivity.this.initData();
    }
  }

  private void collapseToolbar() {

    int toolBarHeight = DensityUtil.getActionBarSize(CityListActivity.this);
    ValueAnimator valueHeightAnimator = ValueAnimator.ofInt(contentViewHeight, toolBarHeight);
    valueHeightAnimator.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
    valueHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        ViewGroup.LayoutParams layoutParams = titleTv.getLayoutParams();
        layoutParams.height = (Integer) animation.getAnimatedValue();
        titleTv.setLayoutParams(layoutParams);
      }
    });

    valueHeightAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        CityListActivity.this.titleTv.setText(title);
        CityListActivity.this.initData();
      }
    });
    valueHeightAnimator.start();
  }

  private void closeRefresh() {
    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
  }

  private void initData() {
    this.cityListPresenter.loadData();
  }

  @Override public void showDataList(Observable<List<CityEntity>> data) {
    CityListActivity.this.closeRefresh();
    data.subscribe(this.cityListAdapter);
  }

  @Override public void showError(String errorMessage) {
    CityListActivity.this.closeRefresh();
    Toast.makeText(CityListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    this.cityListPresenter.detachView();
  }
}
