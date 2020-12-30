package com.flitzen.rmapp.AdaptersAndItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.R;

import java.util.ArrayList;
import java.util.List;

public class SilverSubCategoryAdapter extends RecyclerView.Adapter<SilverSubCategoryAdapter.ViewHolder> {

    List<SilverSubCategoryItems> itemsList = new ArrayList<>();
    Context context;
    SetOnItemClick setOnItemClick;

    public SilverSubCategoryAdapter(Context context, List<SilverSubCategoryItems> itemses) {
        this.context = context;
        this.itemsList = itemses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_silver_sub_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SilverSubCategoryItems item = itemsList.get(position);
        holder.txtName.setText(item.name);
        if (Helper.isNullOrEmpty(item.image)) {
            holder.imageView.setImageResource(R.drawable.img_no_image);
        } else {
            Glide.with(context)
                    .load(item.image)
                    .placeholder(R.drawable.img_loading_placeholder)
                    .error(R.drawable.img_no_image)
                    .into(holder.imageView);
        }

        holder.imgFavorite.setVisibility(View.GONE);

        /*if (item.setFavorite) {
            holder.imgFavorite.setVisibility(View.VISIBLE);
        } else {
            holder.imgFavorite.setVisibility(View.GONE);
        }*/

        /*if (item.isFavorite) {
            holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
            holder.imgFavorite.setColorFilter(ContextCompat.getColor(context, R.color.favoriteIcon), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            holder.imgFavorite.setColorFilter(ContextCompat.getColor(context, R.color.unfavoriteIcon), android.graphics.PorterDuff.Mode.SRC_IN);
        }*/

        /*holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setOnItemClick != null)
                    setOnItemClick.onItemFavoriteClick(position);
            }
        });*/

        holder.viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setOnItemClick != null)
                    setOnItemClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View viewMain;
        ImageView imageView, imgFavorite;
        TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            viewMain = itemView.findViewById(R.id.view_silver_sub_cat_a_main);
            imageView = itemView.findViewById(R.id.img_silver_sub_cat_a_image);
            imgFavorite = itemView.findViewById(R.id.img_silver_sub_cat_a_favorite);
            txtName = itemView.findViewById(R.id.txt_silver_sub_cat_a_name);
        }
    }

    boolean isOdd(int val) {
        return (val & 0x01) != 0;
    }

    public void SetOnItemClickListner(SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
    }

    public interface SetOnItemClick {
        public void onItemClick(int position);

        public void onItemFavoriteClick(int position);
    }
}
