package com.example.ultrafit.views.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import com.example.common.Constants;
import com.example.common.Util.BitmapUtil;
import com.example.model.bean.entity.MovieEntity;
import com.example.ultrafit.R;
import com.example.ultrafit.adapter.MovieAdapter;
import com.example.ultrafit.presenter.MovieListPresenter;
import com.example.ultrafit.presenter.MovieListPresenterImp;
import com.example.ultrafit.ui.MarginDecoration;
import com.example.ultrafit.views.ListView;
import java.util.List;
import rx.Observable;

public class MovieListActivity extends BaseActivity implements ListView<MovieEntity> {

  private static final String START_LOCATION_Y = "START_LOCATION_Y";
  private static final String CITY_ID = "CITY_ID";

  @NonNull @Bind(R.id.movie_layout_root_view) protected ViewGroup rootView;

  @NonNull @Bind(R.id.movie_layout_content_fl) protected FrameLayout contentLayout;
  @NonNull @Bind(R.id.movie_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @Bind(R.id.movie_layout_rv) protected RecyclerView recyclerView;
  @NonNull @Bind(R.id.movie_layout_viewstub) protected ViewStub viewStub;

  private MovieAdapter movieAdapter;
  private StaggeredGridLayoutManager staggeredGridLayoutManager;
  private MovieListPresenter<MovieEntity> movieListPresenter;

  private View itemView;
  private ImageView blurIv;

  private int location;
  private String cityId;

  private MovieAdapter.Callback callback = new MovieAdapter.Callback() {

    @Override public void onItemClick(int position, ImageView thumbIv, MovieEntity movieEntity) {
      if (viewStub.getParent() != null) {
        MovieListActivity.this.blurIv = (ImageView) viewStub.inflate();
      } else {
        MovieListActivity.this.blurIv.setImageDrawable(null);
        viewStub.setVisibility(View.VISIBLE);
      }
      MovieListActivity.this.navigateToDetail(position, thumbIv, movieEntity);
    }

    @Override public void onError(Throwable error) {
      MovieListActivity.this.closeRefresh();
    }
  };

  private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override public void onRefresh() {
      MovieListActivity.this.initData();
    }
  };

  public static void startFromLocation(AppCompatActivity startingActivity, int startingLocationY, String cityId) {

    Intent intent = new Intent(startingActivity, MovieListActivity.class);
    intent.putExtra(START_LOCATION_Y, startingLocationY);
    intent.putExtra(CITY_ID, cityId);
    startingActivity.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MovieListActivity.this.initView(savedInstanceState);

    this.movieListPresenter = MovieListPresenterImp.createdPresenter();
    this.movieListPresenter.attachView(MovieListActivity.this);
  }

  @Override protected int getLayoutId() {
    return R.layout.movie_activity;
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

    recyclerView.addItemDecoration(new MarginDecoration(MovieListActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(staggeredGridLayoutManager);
    movieAdapter = new MovieAdapter(MovieListActivity.this);
    movieAdapter.setCallback(callback);
    recyclerView.setAdapter(movieAdapter);

    this.cityId = getIntent().getStringExtra(CITY_ID);
    this.location = getIntent().getIntExtra(START_LOCATION_Y, 0);

    if (savedInstanceState == null) {

      ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
      if (viewTreeObserver.isAlive()) {
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            rootView.getViewTreeObserver().removeOnPreDrawListener(this);

            MovieListActivity.this.startEnterAnim(location);
            return true;
          }
        });
      }
    } else {
      MovieListActivity.this.initData();
    }
  }

  private void initData() {
    this.movieListPresenter.loadData(cityId);
  }

  @Override public void showDataList(Observable<List<MovieEntity>> data) {
    MovieListActivity.this.closeRefresh();
    data.subscribe(this.movieAdapter);
  }

  private void closeRefresh() {
    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
  }

  @Override public void showError(String errorMessage) {
    MovieListActivity.this.closeRefresh();
    Toast.makeText(MovieListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
  }

  private void navigateToDetail(int position, View thumbIv, MovieEntity movieEntity) {

    this.itemView = staggeredGridLayoutManager.findViewByPosition(position).findViewById(R.id.movie_item_thumb_iv);
    this.itemView.setVisibility(View.INVISIBLE);

    BitmapUtil.blurImage(MovieListActivity.this, this.blurIv, BitmapUtil.retrieveScreen(MovieListActivity.this));

    Rect startBounds = new Rect();
    thumbIv.getGlobalVisibleRect(startBounds);
    Point globalOffset = new Point();
    Rect rootRect = new Rect();
    rootView.getGlobalVisibleRect(rootRect, globalOffset);
    startBounds.offset(-globalOffset.x, -globalOffset.y);

    DetailActivity.navigateToActivity(MovieListActivity.this, startBounds, globalOffset, movieEntity);
    overridePendingTransition(0, 0);
  }

  private void startEnterAnim(int startLocationY) {

    ViewCompat.setPivotY(contentLayout, startLocationY);
    ViewCompat.setScaleY(contentLayout, 0.0f);

    ViewCompat
        .animate(contentLayout)
        .scaleY(1.0f)
        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
        .setInterpolator(new AccelerateInterpolator())
        .withLayer()
        .setListener(new ViewPropertyAnimatorListenerAdapter() {
          @Override public void onAnimationEnd(View view) {
            MovieListActivity.this.initData();
          }
        });
  }

  @Override protected void onPostResume() {
    super.onPostResume();

    if (this.itemView != null) this.itemView.setVisibility(View.VISIBLE);
    this.viewStub.setVisibility(View.GONE);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    this.movieListPresenter.detachView();
  }
}
