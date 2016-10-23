package com.smartdengg.presentation.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.smartdengg.model.entity.CityEntity;
import com.smartdengg.presentation.R;
import java.util.List;
import rx.Observer;
import rx.RxDebounceClick;
import rx.functions.Action1;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ItemHolder>
    implements Observer<List<CityEntity>> {

  private Context context;
  private List<CityEntity> items;
  private Callback callback;
  private boolean animationsLocked;
  private int lastAnimatedPosition;

  public CityListAdapter(Context context) {
    this.context = context;
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.city_item, parent, false));
  }

  @Override public void onBindViewHolder(ItemHolder holder, int position) {

    CityListAdapter.this.bindValue(holder, position);
    CityListAdapter.this.runEnterAnimation(holder.itemView, position);
  }

  private void bindValue(final ItemHolder holder, final int position) {
    final CityEntity cityEntity = items.get(position);

    holder.nameTv.setText(cityEntity.getCityName());
    RxDebounceClick.onClick(holder.itemView).forEach(new Action1<Void>() {
      @Override public void call(Void aVoid) {
        if (callback != null) callback.onItemClick(holder.itemView, cityEntity);
      }
    });
  }

  private void runEnterAnimation(View itemView, int position) {

    if (animationsLocked) return;

    if (position > lastAnimatedPosition) {
      CityListAdapter.this.lastAnimatedPosition = position;

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
              CityListAdapter.this.animationsLocked = true;
            }
          });
    }
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public void onCompleted() {
    CityListAdapter.this.notifyDataSetChanged();
  }

  @Override public void onError(Throwable e) {
    if (callback != null) this.callback.onError(e);
  }

  @Override public void onNext(List<CityEntity> movieEntities) {
    this.items = movieEntities;
  }

  public class ItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.city_item_name_tv) TextView nameTv;

    public ItemHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(ItemHolder.this, itemView);
    }
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public interface Callback {

    void onItemClick(View itemView, CityEntity cityEntity);

    void onError(Throwable error);
  }
}
