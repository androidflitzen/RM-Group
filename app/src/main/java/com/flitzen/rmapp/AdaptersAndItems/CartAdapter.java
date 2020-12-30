package com.flitzen.rmapp.AdaptersAndItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<CartItems> itemsList = new ArrayList<>();
    Context context;
    SetOnItemClick setOnItemClick;

    public CartAdapter(Context context, List<CartItems> itemses) {
        this.context = context;
        this.itemsList = itemses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CartItems item = itemsList.get(position);
        if (Helper.isNullOrEmpty(item.imgUrl)) {
            holder.imgProduct.setImageResource(R.drawable.img_no_image);
        } else {
            Glide.with(context)
                    .load(item.imgUrl)
                    .placeholder(R.drawable.img_loading_placeholder)
                    .error(R.drawable.img_no_image)
                    .into(holder.imgProduct);
        }

        holder.txtName.setText(item.categoryName);
        /*String variationString = "";
        if (!Helper.isNullOrEmpty(item.variation1))
            variationString += (variationString.isEmpty() ? "Variation 1: " + item.variation1 : " | Variation 1: " + item.variation1);
        if (!Helper.isNullOrEmpty(item.variation2))
            variationString += (variationString.isEmpty() ? "Variation 2: " + item.variation2 : " | Variation 2: " + item.variation2);
        if (!Helper.isNullOrEmpty(item.variation3))
            variationString += (variationString.isEmpty() ? "Variation 3: " + item.variation3 : " | Variation 3: " + item.variation3);
        if (!Helper.isNullOrEmpty(item.variation4))
            variationString += (variationString.isEmpty() ? "Variation 4: " + item.variation4 : " | Variation 4: " + item.variation4);
        if (!Helper.isNullOrEmpty(item.variation5))
            variationString += (variationString.isEmpty() ? "Variation 5: " + item.variation5 : " | Variation 5: " + item.variation5);
        if (!Helper.isNullOrEmpty(item.variation6))
            variationString += (variationString.isEmpty() ? "Variation 6: " + item.variation6 : " | Variation 6: " + item.variation6);
        if (!Helper.isNullOrEmpty(item.variation7))
            variationString += (variationString.isEmpty() ? "Variation 7: " + item.variation7 : " | Variation 7: " + item.variation7);
        if (!Helper.isNullOrEmpty(item.variation8))
            variationString += (variationString.isEmpty() ? "Variation 8: " + item.variation8 : " | Variation 8: " + item.variation8);*/

        holder.txtVariation.setText(item.variationString);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setOnItemClick != null)
                    setOnItemClick.onItemRemoved(position);
            }
        });

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
        ImageView imgProduct, imgDelete;
        TextView txtName, txtVariation;

        public ViewHolder(View itemView) {
            super(itemView);
            viewMain = itemView.findViewById(R.id.view_cart_a_main);
            imgProduct = itemView.findViewById(R.id.img_cart_a_product);
            imgDelete = itemView.findViewById(R.id.img_cart_a_delete);
            txtName = itemView.findViewById(R.id.txt_cart_a_name);
            txtVariation = itemView.findViewById(R.id.txt_cart_a_variation);
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

        public void onItemRemoved(int position);
    }
}
