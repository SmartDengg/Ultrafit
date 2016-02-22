package com.smartdengg.ultrafit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartdengg.ultrafit.bean.entity.MovieEntity;
import com.smartdengg.ultrafit.bean.request.MovieIdRequest;
import com.smartdengg.ultrafit.delegate.SubscriberAdapter;
import com.smartdengg.ultrafit.domain.ProductUsecase;
import com.smartdengg.ultrafit.rx.RxExtension;
import com.smartdengg.ultrafit.view.MarginDecoration;
import com.smartdengg.ultrafit.view.MovieAdapter;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import java.util.List;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends RxAppCompatActivity {

  private static int[] colors = new int[] {
      android.R.color.holo_blue_light, android.R.color.holo_purple, android.R.color.holo_red_light
  };

  @NonNull @Bind(R.id.main_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @NonNull @Bind(R.id.main_layout_rv) protected RecyclerView recyclerView;

  private MovieIdRequest movieIdRequest;
  private MovieAdapter movieAdapter;

  private int pageIndex = 1;
  private volatile boolean isEndless = true;

  private CompositeSubscription compositeSubscription = new CompositeSubscription();

  private MovieAdapter.Callback callback = new MovieAdapter.Callback() {
    @Override public void onNoMoreData() {

      MainActivity.this.isEndless = false;
      Toast.makeText(MainActivity.this, "无更多数据", Toast.LENGTH_SHORT).show();
    }

    @Override public void onError(Throwable error) {

    }
  };

  private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override public void onRefresh() {
      MainActivity.this.initData();
    }
  };

  private void initData() {

    int itemCount = 10;
    this.pageIndex = 0;
    movieIdRequest = new MovieIdRequest("hot", pageIndex, itemCount);

    compositeSubscription.add(ProductUsecase
                                  .getMovieList(movieIdRequest)
                                  .doOnTerminate(new Action0() {
                                    @Override public void call() {
                                      if (MainActivity.this.swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                      }
                                    }
                                  })
                                  .compose(MainActivity.this.<List<MovieEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                                  .subscribe(new SubscriberAdapter<List<MovieEntity>>() {
                                    @Override public void onNext(List<MovieEntity> productEntities) {
                                      MainActivity.this.movieAdapter.updateItems(productEntities);
                                    }
                                  }));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(MainActivity.this);

    MainActivity.this.initView();
    MainActivity.this.initData();
  }

  private void initView() {

    swipeRefreshLayout.setColorSchemeResources(MainActivity.colors);
    swipeRefreshLayout.setOnRefreshListener(listener);
    swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        swipeRefreshLayout.setRefreshing(true);
      }
    });

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    linearLayoutManager.setSmoothScrollbarEnabled(true);

    recyclerView.addItemDecoration(new MarginDecoration(MainActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(linearLayoutManager);
    movieAdapter = new MovieAdapter(MainActivity.this);
    recyclerView.setAdapter(movieAdapter);

    movieAdapter.setCallback(callback);

    compositeSubscription.add(
        RxExtension
            .loadMoreEvent(recyclerView)
            .concatMap(new Func1<Void, Observable<List<MovieEntity>>>() {
              @Override public Observable<List<MovieEntity>> call(Void aVoid) {

                /*if (MainActivity.this.isEndless) {
                  movieIdRequest.setOffset(++pageIndex);
                  return ProductUsecase.getMovieList(movieIdRequest);
                }*/
                return Observable.empty();
              }
            })
            .compose(MainActivity.this.<List<MovieEntity>>bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(movieAdapter));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (!compositeSubscription.isUnsubscribed()) compositeSubscription.clear();
    this.movieAdapter.setCallback(null);
    this.swipeRefreshLayout.setOnRefreshListener(null);
    ButterKnife.bind(MainActivity.this);
  }
}
