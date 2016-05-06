package com.smartdengg.presentation.views.activity;

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
import butterknife.Bind;
import butterknife.BindString;
import com.smartdengg.common.Constants;
import com.smartdengg.model.entity.MovieEntity;
import com.smartdengg.presentation.R;
import com.smartdengg.presentation.adapter.MovieAdapter;
import com.smartdengg.presentation.bitmaps.BitmapUtil;
import com.smartdengg.presentation.presenter.MovieListPresenter;
import com.smartdengg.presentation.presenter.MovieListPresenterImp;
import com.smartdengg.presentation.ui.MarginDecoration;
import com.smartdengg.presentation.views.ListView;
import java.util.List;
import rx.Observable;

public class MovieListActivity extends BaseActivity implements ListView<MovieEntity> {

    private static final String START_LOCATION_Y = "START_LOCATION_Y";
    private static final String CITY_ID = "CITY_ID";

    @NonNull
    @BindString(R.string.movie_title)
    protected String title;

    @NonNull
    @Bind(R.id.movie_layout_root_view)
    protected ViewGroup rootView;
    @NonNull
    @Bind(R.id.movie_layout_toolbar)
    protected Toolbar toolbar;

    @NonNull
    @Bind(R.id.movie_layout_content_fl)
    protected FrameLayout contentLayout;
    @NonNull
    @Bind(R.id.movie_layout_srl)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @NonNull
    @Bind(R.id.movie_layout_rv)
    protected RecyclerView recyclerView;
    @NonNull
    @Bind(R.id.movie_layout_vs)
    protected ViewStub viewStub;

    private MovieAdapter movieAdapter = new MovieAdapter(MovieListActivity.this);
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private MovieListPresenter<MovieEntity> movieListPresenter;

    private View itemView;
    private ImageView blurIv;

    private MovieAdapter.Callback callback = new MovieAdapter.Callback() {
        @Override
        public void onItemClick(int position, ImageView thumbIv, MovieEntity movieEntity) {
            if (viewStub.getParent() != null) {
                MovieListActivity.this.blurIv = (ImageView) viewStub.inflate();
            } else {
                MovieListActivity.this.blurIv.setImageDrawable(null);
                viewStub.setVisibility(View.VISIBLE);
            }
            MovieListActivity.this.navigateToDetail(position, thumbIv, movieEntity);
        }

        @Override
        public void onError(Throwable error) {
            MovieListActivity.this.closeRefresh();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            MovieListActivity.this.initData();
        }
    };

    public static void startFromLocation(AppCompatActivity startingActivity, int startingLocationY, String cityId) {
        Intent intent = new Intent(startingActivity, MovieListActivity.class);
        intent.putExtra(START_LOCATION_Y, startingLocationY);
        intent.putExtra(CITY_ID, cityId);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieListActivity.this.initPresenter();
        MovieListActivity.this.initView(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.movie_activity_layout;
    }

    private void initPresenter() {
        this.movieListPresenter = MovieListPresenterImp.createdPresenter();
        this.movieListPresenter.attachView(MovieListActivity.this);
    }

    private void initView(Bundle savedInstanceState) {

        MovieListActivity.this.setSupportActionBar(toolbar);
        MovieListActivity.this.getSupportActionBar()
                              .setTitle(title);
        this.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        this.swipeRefreshLayout.setColorSchemeColors(Constants.colors);
        this.swipeRefreshLayout.setOnRefreshListener(listener);
        this.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        this.staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        this.recyclerView.addItemDecoration(new MarginDecoration(MovieListActivity.this));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        this.movieAdapter.setCallback(callback);
        this.recyclerView.setAdapter(movieAdapter);

        if (savedInstanceState == null) {
            ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        rootView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        MovieListActivity.this.startEnterAnim(getIntent().getIntExtra(START_LOCATION_Y, 0));
                        return true;
                    }
                });
            }
        } else {
            MovieListActivity.this.initData();
        }
    }

    private void initData() {
        this.movieListPresenter.loadData(getIntent().getStringExtra(CITY_ID));
    }

    @Override
    public void showDataList(Observable<List<MovieEntity>> data) {
        MovieListActivity.this.closeRefresh();
        data.subscribe(this.movieAdapter);
    }

    private void closeRefresh() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError(String errorMessage) {
        MovieListActivity.this.closeRefresh();
        if (errorMessage != null) {
            Toast.makeText(MovieListActivity.this, errorMessage, Toast.LENGTH_SHORT)
                 .show();
        }
    }

    private void navigateToDetail(int position, View thumbIv, MovieEntity movieEntity) {

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(position);
        this.itemView = viewHolder.itemView;
        this.itemView.setVisibility(View.INVISIBLE);

        BitmapUtil.blurImage(MovieListActivity.this, this.blurIv, BitmapUtil.retrieveScreenSnapshot(MovieListActivity.this));

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

        toolbar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        contentLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator toolbarInAnim = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, -toolbar.getHeight(), 0.0f);
        ObjectAnimator contentInAnim = ObjectAnimator.ofFloat(contentLayout, View.SCALE_Y, 0.0f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(toolbarInAnim, contentInAnim);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                toolbar.setLayerType(View.LAYER_TYPE_NONE, null);
                contentLayout.setLayerType(View.LAYER_TYPE_NONE, null);
                MovieListActivity.this.initData();
            }
        });

        animatorSet.start();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (this.itemView != null) {
            this.itemView.setVisibility(View.VISIBLE);
        }
        this.viewStub.setVisibility(View.GONE);
    }

    @Override
    protected void exit() {

        toolbar.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        contentLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator toolbarExitAnim = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, 0.0f, -toolbar.getHeight());
        ObjectAnimator contentExitAnim = ObjectAnimator.ofFloat(contentLayout, View.TRANSLATION_Y, 0.0f, contentLayout.getHeight());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.playTogether(toolbarExitAnim, contentExitAnim);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                toolbar.setLayerType(View.LAYER_TYPE_NONE, null);
                contentLayout.setLayerType(View.LAYER_TYPE_NONE, null);
                MovieListActivity.this.finish();
            }
        });

        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.movieListPresenter.detachView();
    }
}
