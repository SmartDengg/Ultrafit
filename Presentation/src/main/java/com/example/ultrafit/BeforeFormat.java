package com.example.ultrafit;

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


@HttpGet(stringUrl = "")
@HttpPost(stringUrl = "")
@SuppressWarnings("")
public class BeforeFormat extends RxAppCompatActivity
{

	@Bind(R.id.city_layout_root_view)
	protected View view;

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

	@Retention(RetentionPolicy.SOURCE)
	@Target(value = { ElementType.FIELD, ElementType.PARAMETER,
	        ElementType.METHOD })
	@StringDef({ android, iOS, 休息视频, 福利, 拓展资源, 前端, 瞎推荐, App })
	public @interface Type
	{
	}



	@SuppressWarnings("")
	@Deprecated
	@NonNull
	public void ChainCode(@IntegerRes @Nullable @NonNull int id, String s1,
	        String s2, String s3)
	{

		if (true)
			Observable.just(1);

		Observable.just(1).map(new Func1<Integer, Integer>()
		{
			@Override
			public Integer call(Integer integer)
			{
				return 1;
			}
		}).flatMap(new Func1<Integer, Observable<Integer>>()
		{
			@Override
			@Deprecated
			@SuppressWarnings("")
			@CallSuper
			public Observable<Integer> call(Integer integer)
			{
				return Observable.just(1);
			}
		}).flatMap(new Func1<Integer, Observable<Integer>>()
		{
			@Override
			public Observable<Integer> call(Integer integer)
			{
				return Observable.just(1);
			}
		}).flatMap(new Func1<Integer, Observable<Integer>>()
		{
			@Override
			public Observable<Integer> call(Integer integer)
			{
				return Observable.just(1);
			}
		}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
		        .observeOn(Schedulers.io()).observeOn(Schedulers.io())
		        .subscribe();

		Picasso.with(this).load("").placeholder(R.drawable.toolbar_placeholder)
		        .error(R.drawable.toolbar_placeholder).noFade().fit()
		        .noPlaceholder().into(new ImageView(this));
	}

	class TestInnerClass
	{
	}
}
