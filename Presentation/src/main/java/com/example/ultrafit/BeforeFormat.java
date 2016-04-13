/*
package com.example.ultrafit;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.media.RemoteController;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
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

@TargetApi(Build.VERSION_CODES.KITKAT)
@HttpGet(stringUrl = "")
@HttpPost(stringUrl = "")
@SuppressWarnings("")
public class BeforeFormat extends RxAppCompatActivity
        implements DialogInterface.OnClickListener, View.OnClickListener, RemoteController.OnClientUpdateListener {

    @Bind(R.id.city_layout_root_view) protected View view;
    @Bind(R.id.city_layout_root_view)
    @SuppressWarnings("")
    protected View view2;
    private static final String android = "Android";
    private static final String iOS = "iOS";
    private static final String 休息视频 = "休息视频";
    private static final String 福利 = "福利";
    private static final String 拓展资源 = "拓展资源";
    private static final String 前端 = "前端";
    private static final String 瞎推荐 = "瞎推荐";
    private static final String App = "App";
    //@Argument(parameter = "", parameter = "", parameter = "", parameter = "", parameter = "",
    //          parameter = "", parameter = "")
    private String s;

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onClientChange(boolean clearing) {
    }

    @Override
    public void onClientPlaybackStateUpdate(int state) {
    }

    @Override
    public void onClientPlaybackStateUpdate(int state, long stateChangeTimeMs, long currentPosMs, float speed) {
    }

    @Override
    public void onClientTransportControlUpdate(int transportControlFlags) {
    }

    @Override
    public void onClientMetadataUpdate(RemoteController.MetadataEditor metadataEditor) {
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(value = { ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
    @StringDef({ android, iOS, 休息视频, 福利, 拓展资源, 前端, 瞎推荐, App })
    public @interface Type {}

    @SuppressWarnings("")
    @Deprecated
    @NonNull
    public void ChainCode(@IntegerRes @Nullable @NonNull int id, String s1, String s2, String s3, @NonNull int id0,
                          @NonNull int id1, @NonNull int id2) {

        if (true) {
            Observable.just(1);
        }

        Observable.just(1)
                  .map(new Func1<Integer, Integer>() {
                      @Override
                      public Integer call(Integer integer) {
                          return 1;
                      }
                  })
                  .flatMap(new Func1<Integer, Observable<Integer>>() {
                      @Override
                      @Deprecated
                      @SuppressWarnings("")
                      @CallSuper
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

        Picasso.with(this).load("").into(new ImageView(this));

        Picasso.with(this)
               .load("")
               .placeholder(R.drawable.toolbar_placeholder)
               .error(R.drawable.toolbar_placeholder)
               .noFade()
               .fit()
               .noPlaceholder()
               .into(new ImageView(this));
    }

    class TestInnerClass {}
}
*/
