package com.smartdengg.ultrafit.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartdengg.ultrafit.R;
import com.smartdengg.ultrafit.bean.entity.MovieEntity;
import com.squareup.picasso.Picasso;
import java.util.List;
import rx.Observer;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ItemHolder> implements Observer<List<MovieEntity>> {

  private Context context;
  private List<MovieEntity> items;
  private Callback callback;

  public MovieAdapter(Context context) {
    this.context = context;
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false));
  }

  @Override public void onBindViewHolder(ItemHolder holder, int position) {

    MovieEntity movieEntity = items.get(position);

    holder.nameTv.setText(movieEntity.getMovieName());
    holder.descriptionTv.setText(movieEntity.getMovieDescription());
    holder.categoryTv.setText(movieEntity.getMovieCategory());
    holder.directorTv.setText("导演:" + movieEntity.getMovieDirector());
    holder.actorTv.setText("演员:" + movieEntity.getMovieActor());

    Picasso
        .with(context)
        .load(items.get(position).getMovieThumbUrl())
        .noFade()
        .placeholder(R.drawable.ic_holder_icon)
        .into(holder.thumbIv);
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  public void updateItems(List<MovieEntity> movieEntities) {

    this.items = movieEntities;
    MovieAdapter.this.notifyDataSetChanged();
  }

  @Override public void onCompleted() {
    /*never invoke*/
  }

  @Override public void onError(Throwable e) {
    if (callback != null) this.callback.onError(e);
  }

  @Override public void onNext(List<MovieEntity> productEntities) {

    if (productEntities != null && productEntities.size() == 0) {
      if (callback != null) this.callback.onNoMoreData();
    } else if (productEntities != null) {
      this.items.addAll(productEntities);
      MovieAdapter.this.notifyItemInserted(this.items.size() - productEntities.size());
    }
  }

  public class ItemHolder extends RecyclerView.ViewHolder {

    @Nullable @Bind(R.id.movie_item_thumb_iv) ImageView thumbIv;
    @Nullable @Bind(R.id.movie_item_name_tv) TextView nameTv;
    @Nullable @Bind(R.id.movie_item_description_tv) TextView descriptionTv;

    @Nullable @Bind(R.id.movie_item_category_tv) TextView categoryTv;
    @Nullable @Bind(R.id.movie_item_director_tv) TextView directorTv;
    @Nullable @Bind(R.id.movie_item_actor_tv) TextView actorTv;

    public ItemHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(ItemHolder.this, itemView);
    }
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public interface Callback {

    void onNoMoreData();

    void onError(Throwable error);
  }
}
