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
import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.presentation.R;
import com.smartdengg.presentation.adapter.CityListAdapter;
import com.smartdengg.presentation.presenter.CityListPresenter;
import com.smartdengg.presentation.presenter.CityListPresenterImp;
import com.smartdengg.presentation.ui.MarginDecoration;
import com.smartdengg.presentation.views.ViewInterface;
import java.util.List;
import rx.Observable;

/**
 * Created by SmartDengg on 2016/2/24.
 */
public class CityActivityInterface extends BaseActivity implements ViewInterface<List<CityEntity>> {

    @NonNull
    @BindString(R.string.city_title)
    protected String title;

    @NonNull
    @Bind(R.id.city_layout_root_view)
    protected ViewGroup rootView;
    @NonNull
    @Bind(R.id.city_layout_toolbar)
    protected Toolbar toolbar;
    @NonNull
    @Bind(R.id.city_layout_srl)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @NonNull
    @Bind(R.id.city_layout_rv)
    protected RecyclerView recyclerView;

    private ActionBar actionBar;

    private CityListAdapter cityListAdapter = new CityListAdapter(CityActivityInterface.this);
    private CityListPresenter<List<CityEntity>> cityListPresenter;

    private CityListAdapter.Callback callback = new CityListAdapter.Callback() {
        @Override
        public void onItemClick(View itemView, CityEntity cityEntity) {
            int location = DensityUtil.getLocationY(itemView);
            MovieActivity.startFromLocation(CityActivityInterface.this, location, cityEntity.getCityId());
            overridePendingTransition(0, 0);
        }

        @Override
        public void onError(Throwable error) {
            CityActivityInterface.this.closeRefresh();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            CityActivityInterface.this.initData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);

        CityActivityInterface.this.initPresenter();
        CityActivityInterface.this.initView(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_layout;
    }

    private void initPresenter() {
        this.cityListPresenter = CityListPresenterImp.createdPresenter();
        this.cityListPresenter.attachView(CityActivityInterface.this);
    }

    private void initView(Bundle savedInstanceState) {

        CityActivityInterface.this.setSupportActionBar(toolbar);
        actionBar = CityActivityInterface.this.getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(null);

        this.swipeRefreshLayout.setColorSchemeColors(Constants.colors);
        this.swipeRefreshLayout.setOnRefreshListener(listener);
        this.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CityActivityInterface.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        this.cityListAdapter.setCallback(callback);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new MarginDecoration(CityActivityInterface.this));
        this.recyclerView.setAdapter(cityListAdapter);

        if (savedInstanceState == null) {

            ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        rootView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        CityActivityInterface.this.collapseToolbar();
                        return true;
                    }
                });
            }
        } else {

            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
            layoutParams.height = DensityUtil.getActionBarSize(CityActivityInterface.this);
            toolbar.setLayoutParams(layoutParams);
            CityActivityInterface.this.initData();
        }
    }

    @SuppressWarnings("unchecked")
    private void collapseToolbar() {

        Integer shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        Integer longAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

        int contentViewHeight = toolbar.getHeight();
        int toolBarHeight = DensityUtil.getActionBarSize(CityActivityInterface.this);
        ValueAnimator valueHeightAnimator = ValueAnimator.ofInt(contentViewHeight, toolBarHeight);
        valueHeightAnimator.setDuration(longAnimTime * 2);
        valueHeightAnimator.setInterpolator(new DecelerateInterpolator(2.0f));
        valueHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
                layoutParams.height = (Integer) animation.getAnimatedValue();
                toolbar.setLayoutParams(layoutParams);
            }
        });

        valueHeightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (actionBar == null) return;
                actionBar.setTitle(title);
                CityActivityInterface.this.initData();
            }
        });
        valueHeightAnimator.setStartDelay(shortAnimTime);
        valueHeightAnimator.start();
    }

    private void initData() {
        this.cityListPresenter.loadData();
    }

    @Override
    public void showData(Observable<List<CityEntity>> data) {
        CityActivityInterface.this.closeRefresh();
        data.subscribe(this.cityListAdapter);
    }

    @Override
    public void showError(String errorMessage) {
        CityActivityInterface.this.closeRefresh();
        if (errorMessage != null) {
            Toast.makeText(CityActivityInterface.this, errorMessage, Toast.LENGTH_SHORT)
                 .show();
        }
    }

    @Override
    protected void exit() {
        CityActivityInterface.this.finish();
    }

    private void closeRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.cityListPresenter.detachView();
    }
}
