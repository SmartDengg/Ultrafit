package com.smartdengg.ultrafit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartdengg.ultrafit.bean.entity.ProductEntity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import rx.Observer;

/**
 * Created by SmartDengg on 2016/2/21.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemHolder>
    implements Observer<List<ProductEntity>> {

  private Context context;
  private List<ProductEntity> items = new ArrayList<>();

  public ProductAdapter(Context context) {
    this.context = context;
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.product_item, parent, false));
  }

  @Override public void onBindViewHolder(ItemHolder holder, int position) {

    ProductEntity productEntity = items.get(position);

    holder.nameTv.setText(productEntity.getGoodName());
    holder.priceTv.setText(productEntity.getGoodPrice());

    Picasso
        .with(context)
        .load(Constants.BASE_URL + items.get(position).getGoodThumbUrl())
        .noFade()
        .placeholder(R.drawable.ic_holder_icon)
        .into(holder.thumbIv);
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public void onCompleted() {
  }

  @Override public void onError(Throwable e) {

  }

  @Override public void onNext(List<ProductEntity> productEntities) {

    if (this.items == null || this.items.size() == 0) {
      this.items.addAll(productEntities);
      ProductAdapter.this.notifyDataSetChanged();
    }
  }

  public class ItemHolder extends RecyclerView.ViewHolder {

    @Nullable @Bind(R.id.product_item_root_view) LinearLayout rootView;
    @Nullable @Bind(R.id.product_item_thumb_iv) ImageView thumbIv;
    @Nullable @Bind(R.id.product_item_name_tv) TextView nameTv;
    @Nullable @Bind(R.id.product_item_price_tv) TextView priceTv;

    public ItemHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(ItemHolder.this, itemView);
    }
  }
}
