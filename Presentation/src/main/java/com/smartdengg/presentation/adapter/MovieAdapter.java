package com.smartdengg.presentation.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.smartdengg.domain.entity.MovieEntity;
import com.smartdengg.presentation.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import rx.Observer;
import rx.RxDebounceClick;
import rx.functions.Action1;

/**
 * Created by SmartDengg on 2016/2/21.
 */

@SuppressWarnings("all") public class MovieAdapter
    extends RecyclerView.Adapter<MovieAdapter.ItemHolder> implements Observer<List<MovieEntity>> {

  private Context context;
  private List<MovieEntity> items;
  private Callback callback;
  private boolean animationsLocked;
  private int lastAnimatedPosition;

  public MovieAdapter(Context context) {
    this.context = context;
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false));
  }

  @Override public void onBindViewHolder(ItemHolder holder, int position) {

    MovieAdapter.this.bindValue(holder, position);
    MovieAdapter.this.runEnterAnimation(holder.itemView, position);
  }

  private void bindValue(final ItemHolder holder, final int position) {
    final MovieEntity movieEntity = items.get(position);

    holder.nameTv.setText(movieEntity.getMovieName());
    holder.scoreTv.setText(movieEntity.getMovieScore() + " 分");

    RxDebounceClick.onClick(holder.itemView).forEach(new Action1<Void>() {
      @Override public void call(Void aVoid) {
        if (callback != null) callback.onItemClick(position, holder.thumbIv, movieEntity);
      }
    });

    Picasso.with(context)
        .load(items.get(position).getMovieThumbUrl())
        .noFade()
        .into(holder.thumbIv);
  }

  private void runEnterAnimation(View itemView, int position) {

    if (animationsLocked) return;

    if (position > lastAnimatedPosition) {
      MovieAdapter.this.lastAnimatedPosition = position;

      ViewCompat.setTranslationY(itemView, 100);
      ViewCompat.setAlpha(itemView, 0.0f);

      ViewCompat.animate(itemView)
          .translationY(0.0f)
          .alpha(1.0f)
          .setStartDelay(position * 20)
          .setInterpolator(new DecelerateInterpolator(2.0f))
          .setDuration(context.getResources().getInteger(android.R.integer.config_mediumAnimTime))
          .withLayer()
          .setListener(new ViewPropertyAnimatorListenerAdapter() {
            @Override public void onAnimationEnd(View view) {
              MovieAdapter.this.animationsLocked = true;
            }
          });
    }
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public void onCompleted() {
    MovieAdapter.this.notifyDataSetChanged();
  }

  @Override public void onError(Throwable e) {
    if (callback != null) this.callback.onError(e);
  }

  @Override public void onNext(List<MovieEntity> movieEntities) {
    this.items = movieEntities;
  }

  public class ItemHolder extends RecyclerView.ViewHolder {

    @Nullable @BindView(R.id.movie_item_thumb_iv) ImageView thumbIv;
    @Nullable @BindView(R.id.movie_item_name_tv) TextView nameTv;
    @Nullable @BindView(R.id.movie_item_score_tv) TextView scoreTv;

    public ItemHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(ItemHolder.this, itemView);
    }
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public interface Callback {

    void onItemClick(int position, ImageView thumbIv, MovieEntity movieEntity);

    void onError(Throwable error);
  }
}
