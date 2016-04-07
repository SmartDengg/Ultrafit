package com.example.ultrafit;

import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import com.example.common.ultrafit.annotation.HttpGet;
import com.example.common.ultrafit.annotation.HttpPost;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@HttpGet(stringUrl = "")
@HttpPost(stringUrl = "")
@SuppressWarnings("")
public class AfterFormat extends RxAppCompatActivity
        implements View.OnClickListener, Picasso.Listener, View.OnTouchListener,
                   RecyclerView.OnItemTouchListener {

    private static final String android = "Android";
    private static final String iOS = "iOS";
    private static final String 休息视频 = "休息视频";
    private static final String 福利 = "福利";
    private static final String 拓展资源 = "拓展资源";
    private static final String 前端 = "前端";
    private static final String 瞎推荐 = "瞎推荐";
    private static final String App = "App";
    @Bind(R.id.city_layout_root_view)
    protected View view1;
    @Bind(R.id.city_layout_root_view)
    @SuppressWarnings("")
    protected View view;
    int[] a = new int[] { 1, 2, 0x0052, 0x0053, 0x0054 };

    @Override
    public void onClick(View v) {}

    @Override
    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    @NonNull
    @SuppressWarnings("")
    @Deprecated
    public void ChainCode(@IntegerRes @Nullable @NonNull int id, String s1, @NonNull String s2,
                          @NonNull String s3, @NonNull String s4, @NonNull String s5, String s6) {

        if (false) {
            Observable.just(1);
        }

        Observable.just(1)
                  .map(new Func1<Integer, Integer>() {
                      @Override
                      @Deprecated
                      @SuppressWarnings("")
                      public Integer call(Integer integer) {
                          return 1;
                      }
                  })
                  .flatMap(new Func1<Integer, Observable<Integer>>() {
                      @Override
                      public Observable<Integer> call(Integer integer) {
                          return Observable.just(1);
                      }
                  })
                  .flatMap(new Func1<Integer, Observable<Integer>>() {
                      @Override
                      public Observable<Integer> call(Integer integer) {
                          return Observable.just(1);
                      }
                  })
                  .flatMap(new Func1<Integer, Observable<Integer>>() {
                      @Override
                      public Observable<Integer> call(Integer integer) {
                          return Observable.just(1);
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(Schedulers.io())
                  .observeOn(Schedulers.io())
                  .observeOn(Schedulers.io())
                  .subscribe();

        Picasso.with(this)
               .load("")
               .into(new ImageView(this));

        Picasso.with(this)
               .load("")
               .placeholder(R.drawable.toolbar_placeholder)
               .error(R.drawable.toolbar_placeholder)
               .noFade()
               .fit()
               .noPlaceholder()
               .into(new ImageView(this));
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(value = { ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
    @StringDef({ android, iOS, 休息视频, 福利, 拓展资源, 前端, 瞎推荐, App })
    public @interface Type {}

    class TestInnerClass {}

    class Text {}

}
