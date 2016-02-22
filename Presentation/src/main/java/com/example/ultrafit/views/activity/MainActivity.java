package com.example.ultrafit.views.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import com.example.common.Constants;
import com.example.common.Util.BitmapUtil;
import com.example.common.Util.DensityUtil;
import com.example.model.bean.entity.MovieEntity;
import com.example.ultrafit.R;
import com.example.ultrafit.adapter.MovieAdapter;
import com.example.ultrafit.presenter.ListPresenter;
import com.example.ultrafit.presenter.ListPresenterImp;
import com.example.ultrafit.ui.MarginDecoration;
import com.example.ultrafit.views.ListView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import java.util.List;
import rx.Observable;

public class MainActivity extends RxAppCompatActivity implements ListView<MovieEntity> {

  @NonNull @BindString(R.string.app_name) protected String title;

  @NonNull @Bind(R.id.main_layout_root_rl) protected RelativeLayout rootView;
  @NonNull @Bind(R.id.main_layout_title_tv) protected TextView titleTv;

  @NonNull @Bind(R.id.main_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @Bind(R.id.main_layout_rv) protected RecyclerView recyclerView;
  @NonNull @Bind(R.id.main_layout_viewstub) protected ViewStub viewStub;

  private int contentViewHeight;
  private MovieAdapter movieAdapter;
  private StaggeredGridLayoutManager staggeredGridLayoutManager;
  private ListPresenter<MovieEntity> listPresenter;

  private View itemView;
  private ImageView blurIv;

  private MovieAdapter.Callback callback = new MovieAdapter.Callback() {

    @Override public void onItemClick(int position, ImageView thumbIv, MovieEntity movieEntity) {

      if (viewStub.getParent() != null) {
        MainActivity.this.blurIv = (ImageView) viewStub.inflate();
      } else {
        viewStub.setVisibility(View.VISIBLE);
      }

      MainActivity.this.navigateToDetail(position, thumbIv, movieEntity);
    }

    @Override public void onError(Throwable error) {
      MainActivity.this.closeRefresh();
    }
  };

  private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override public void onRefresh() {
      MainActivity.this.initData();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    getWindow().setBackgroundDrawable(null);
    ButterKnife.bind(MainActivity.this);

    MainActivity.this.initView(savedInstanceState);

    this.listPresenter = ListPresenterImp.createdPresenter();
    this.listPresenter.attachView(MainActivity.this);
  }

  private void initView(Bundle savedInstanceState) {

    swipeRefreshLayout.setColorSchemeResources(Constants.colors);
    swipeRefreshLayout.setOnRefreshListener(listener);
    swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        swipeRefreshLayout.setRefreshing(true);
      }
    });
    staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    recyclerView.addItemDecoration(new MarginDecoration(MainActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(staggeredGridLayoutManager);
    movieAdapter = new MovieAdapter(MainActivity.this);
    movieAdapter.setCallback(callback);
    recyclerView.setAdapter(movieAdapter);

    if (savedInstanceState == null) {
      this.titleTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override public boolean onPreDraw() {
          titleTv.getViewTreeObserver().removeOnPreDrawListener(this);

          contentViewHeight = titleTv.getHeight();
          MainActivity.this.collapseToolbar();
          return true;
        }
      });
    } else {
      MainActivity.this.initData();
    }
  }

  private void collapseToolbar() {

    int toolBarHeight = DensityUtil.getActionBarSize(MainActivity.this);
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
        MainActivity.this.titleTv.setText(title);
        MainActivity.this.initData();
      }
    });
    valueHeightAnimator.start();
  }

  private void initData() {
    this.listPresenter.loadData();
  }

  @Override public void showMovieList(Observable<List<MovieEntity>> data) {
    MainActivity.this.closeRefresh();
    data.subscribe(movieAdapter);
  }

  private void closeRefresh() {
    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
  }

  @Override public void showError(String errorMessage) {
    MainActivity.this.closeRefresh();
    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
  }

  private void navigateToDetail(int position, View thumbIv, MovieEntity movieEntity) {

    this.itemView = staggeredGridLayoutManager.findViewByPosition(position).findViewById(R.id.movie_item_thumb_iv);
    this.itemView.setVisibility(View.INVISIBLE);

    BitmapUtil.blurImage(MainActivity.this, blurIv, BitmapUtil.retrieveScreen(MainActivity.this));

    Rect startBounds = new Rect();
    thumbIv.getGlobalVisibleRect(startBounds);
    Point globalOffset = new Point();
    Rect rootRect = new Rect();
    rootView.getGlobalVisibleRect(rootRect, globalOffset);
    startBounds.offset(-globalOffset.x, -globalOffset.y);

    DetailActivity.navigateToActivity(MainActivity.this, startBounds, globalOffset, movieEntity);
    overridePendingTransition(0, 0);
  }

  @Override protected void onPostResume() {
    super.onPostResume();

    if (this.itemView != null) this.itemView.setVisibility(View.VISIBLE);
    this.viewStub.setVisibility(View.GONE);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(MainActivity.this);
    this.listPresenter.detachView();
  }
}
