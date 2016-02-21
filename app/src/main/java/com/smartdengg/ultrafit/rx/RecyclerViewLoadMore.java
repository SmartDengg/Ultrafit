package com.smartdengg.ultrafit.rx;

import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * Created by Joker on 2015/11/19.
 */
public class RecyclerViewLoadMore implements Observable.OnSubscribe<Boolean> {

  private final RecyclerView recyclerView;

  public RecyclerViewLoadMore(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {

    /*check current thread*/
    if (Looper.getMainLooper() != Looper.myLooper()) {
      throw new IllegalStateException(
          "Must be called from the main thread. Was: " + Thread.currentThread());
    }

    final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();

        int pastVisibleItems;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
          pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
          pastVisibleItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else {

          int[] lastPositions = new int[((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).getSpanCount()];

          pastVisibleItems =
              ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(
                  lastPositions)[0];
        }

       /* Log.e("Joker", "pastVisibleItems: " + pastVisibleItems);
        Log.e("Joker", "visibleItemCount: " + visibleItemCount);
        Log.e("Joker", "totalItemCount: " + totalItemCount);*/

        if (!subscriber.isUnsubscribed()) {
          if (totalItemCount != 0 && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
            subscriber.onNext(true);
          } else {
            subscriber.onNext(false);
          }
        }
      }
    };

    recyclerView.addOnScrollListener(scrollListener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        recyclerView.removeOnScrollListener(scrollListener);
      }
    });
  }
}
