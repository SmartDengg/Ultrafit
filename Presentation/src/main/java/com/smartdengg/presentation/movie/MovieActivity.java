package com.smartdengg.presentation.movie;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.BindView;
import com.smartdengg.common.Constants;
import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.presentation.BaseActivity;
import com.smartdengg.presentation.R;
import com.smartdengg.presentation.adapter.MovieAdapter;
import com.smartdengg.presentation.bitmaps.BitmapUtil;
import com.smartdengg.presentation.detail.DetailActivity;
import com.smartdengg.presentation.ui.MarginDecoration;
import java.util.List;
import rx.Observable;

public class MovieActivity extends BaseActivity implements MovieContract.View<List<MovieEntity>> {

  private static final String START_LOCATION_Y = "START_LOCATION_Y";
  private static final String CITY_ID = "CITY_ID";

  @NonNull @BindString(R.string.movie_title) protected String title;

  @NonNull @BindView(R.id.movie_layout_root_view) protected ViewGroup rootView;
  @NonNull @BindView(R.id.movie_layout_toolbar) protected Toolbar toolbar;

  @NonNull @BindView(R.id.movie_layout_content_fl) protected FrameLayout contentLayout;
  @NonNull @BindView(R.id.movie_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @BindView(R.id.movie_layout_rv) protected RecyclerView recyclerView;
  @NonNull @BindView(R.id.movie_layout_vs) protected ViewStub viewStub;

  private MovieAdapter movieAdapter = new MovieAdapter(MovieActivity.this);
  private MovieContract.Presenter<List<MovieEntity>> moviePresenter;

  private View itemView;
  private ImageView blurIv;

  private MovieAdapter.Callback callback = new MovieAdapter.Callback() {
    @Override public void onItemClick(int position, ImageView thumbIv, MovieEntity movieEntity) {
      if (viewStub.getParent() != null) {
        MovieActivity.this.blurIv = (ImageView) viewStub.inflate();
      } else {
        MovieActivity.this.blurIv.setImageDrawable(null);
        viewStub.setVisibility(View.VISIBLE);
      }
      MovieActivity.this.navigateToDetail(position, thumbIv, movieEntity);
    }

    @Override public void onError(Throwable error) {
      MovieActivity.this.closeRefresh();
    }
  };

  private SwipeRefreshLayout.OnRefreshListener listener =
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override public void onRefresh() {
          MovieActivity.this.initData();
        }
      };

  public static void startFromLocation(AppCompatActivity startingActivity, int startingLocationY,
      String cityId) {
    Intent intent = new Intent(startingActivity, MovieActivity.class);
    intent.putExtra(START_LOCATION_Y, startingLocationY);
    intent.putExtra(CITY_ID, cityId);
    startingActivity.startActivity(intent);
    startingActivity.overridePendingTransition(0, 0);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MovieActivity.this.initPresenter();
    MovieActivity.this.initView(savedInstanceState);
  }

  @Override protected int getLayoutId() {
    return R.layout.movie_activity_layout;
  }

  private void initPresenter() {
    this.moviePresenter = MoviePresenterImp.createdPresenter();
    this.moviePresenter.attachView(MovieActivity.this);
  }

  @SuppressWarnings("all") private void initView(Bundle savedInstanceState) {

    MovieActivity.this.setSupportActionBar(toolbar);

    MovieActivity.this.getSupportActionBar().setTitle(title);
    this.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

    this.swipeRefreshLayout.setColorSchemeColors(Constants.colors);
    this.swipeRefreshLayout.setOnRefreshListener(listener);
    this.swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        swipeRefreshLayout.setRefreshing(true);
      }
    });
    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    this.recyclerView.addItemDecoration(new MarginDecoration(MovieActivity.this));
    this.recyclerView.setHasFixedSize(true);
    this.recyclerView.setLayoutManager(staggeredGridLayoutManager);
    this.movieAdapter.setCallback(callback);
    this.recyclerView.setAdapter(movieAdapter);

    if (savedInstanceState == null) {
      ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
      if (viewTreeObserver.isAlive()) {
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            rootView.getViewTreeObserver().removeOnPreDrawListener(this);
            MovieActivity.this.startEnterAnim(getIntent().getIntExtra(START_LOCATION_Y, 0));
            return true;
          }
        });
      }
    } else {
      MovieActivity.this.initData();
    }
  }

  private void initData() {
    this.moviePresenter.loadData(getIntent().getStringExtra(CITY_ID));
  }

  @Override public void showData(Observable<List<MovieEntity>> data) {
    MovieActivity.this.closeRefresh();
    data.subscribe(this.movieAdapter);
  }

  private void closeRefresh() {
    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }
  }

  @Override public void showError(String errorMessage) {
    MovieActivity.this.closeRefresh();
    if (errorMessage != null) {
      Toast.makeText(MovieActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
  }

  private void navigateToDetail(int position, View thumbIv, MovieEntity movieEntity) {

    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(position);
    this.itemView = viewHolder.itemView;
    this.itemView.setVisibility(View.INVISIBLE);

    BitmapUtil.blurImage(MovieActivity.this, this.blurIv,
        BitmapUtil.retrieveScreenSnapshot(MovieActivity.this));

    Rect startBounds = new Rect();
    thumbIv.getGlobalVisibleRect(startBounds);
    Point globalOffset = new Point();
    Rect rootRect = new Rect();
    rootView.getGlobalVisibleRect(rootRect, globalOffset);
    startBounds.offset(-globalOffset.x, -globalOffset.y);

    DetailActivity.navigateToActivity(MovieActivity.this, startBounds, globalOffset, movieEntity);
  }

  private void startEnterAnim(int startLocationY) {

    ViewCompat.setPivotY(contentLayout, startLocationY);

    toolbar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    contentLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

    ObjectAnimator toolbarInAnim =
        ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, -toolbar.getHeight(), 0.0f);
    ObjectAnimator contentInAnim = ObjectAnimator.ofFloat(contentLayout, View.SCALE_Y, 0.0f, 1.0f);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
    animatorSet.setInterpolator(new LinearInterpolator());
    animatorSet.playTogether(toolbarInAnim, contentInAnim);
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {

        toolbar.setLayerType(View.LAYER_TYPE_NONE, null);
        contentLayout.setLayerType(View.LAYER_TYPE_NONE, null);
        MovieActivity.this.initData();
      }
    });

    animatorSet.start();
  }

  @Override protected void onPostResume() {
    super.onPostResume();

    if (this.itemView != null) {
      this.itemView.setVisibility(View.VISIBLE);
    }
    this.viewStub.setVisibility(View.GONE);
  }

  @Override protected void exit() {

    toolbar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    contentLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

    ObjectAnimator toolbarExitAnim =
        ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, 0.0f, -toolbar.getHeight());
    ObjectAnimator contentExitAnim =
        ObjectAnimator.ofFloat(contentLayout, View.TRANSLATION_Y, 0.0f, contentLayout.getHeight());

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
    animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
    animatorSet.playTogether(toolbarExitAnim, contentExitAnim);
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        toolbar.setLayerType(View.LAYER_TYPE_NONE, null);
        contentLayout.setLayerType(View.LAYER_TYPE_NONE, null);
        MovieActivity.this.finish();
      }
    });

    animatorSet.start();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    this.moviePresenter.detachView();
  }
}
