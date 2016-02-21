package com.smartdengg.ultrafit;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartdengg.ultrafit.bean.entity.ProductEntity;
import com.smartdengg.ultrafit.bean.request.ProductRequest;
import com.smartdengg.ultrafit.delegate.SubscriberAdapter;
import com.smartdengg.ultrafit.domain.ProductUsecase;
import java.util.List;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

  private static int[] colors = new int[] {
      android.R.color.holo_blue_light, android.R.color.holo_purple, android.R.color.holo_red_light
  };

  @Bind(R.id.main_layout_srl) protected SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.main_layout_rv) protected RecyclerView recyclerView;

  private ProductRequest productRequest;
  private ProductAdapter productAdapter;

  private int pageIndex = 1;
  private int itemCount = 10;

  private CompositeSubscription compositeSubscription = new CompositeSubscription();

  private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override public void onRefresh() {

      MainActivity.this.initData();
    }
  };

  private void initData() {
    productRequest = new ProductRequest("GetProductByHot", pageIndex, itemCount);

    compositeSubscription.add(
        ProductUsecase.getProductList(productRequest).subscribe(new SubscriberAdapter<List<ProductEntity>>() {
          @Override public void onNext(List<ProductEntity> productEntities) {
            Observable.just(productEntities).subscribe(productAdapter);
          }

          @Override public void onCompleted() {
            if (MainActivity.this.swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
          }

          @Override public void onError(Throwable e) {
            if (MainActivity.this.swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
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

    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    gridLayoutManager.setSmoothScrollbarEnabled(true);

    recyclerView.addItemDecoration(new MarginDecoration(MainActivity.this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(gridLayoutManager);
    productAdapter = new ProductAdapter(MainActivity.this);
    recyclerView.setAdapter(productAdapter);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (!compositeSubscription.isUnsubscribed()) compositeSubscription.clear();
    this.swipeRefreshLayout.setOnRefreshListener(null);
    ButterKnife.bind(MainActivity.this);
  }
}
