package com.flitzen.rmapp.AdaptersAndItems;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flitzen.rmapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    List<ProductListItems> itemsList = new ArrayList<>();
    Context context;
    SetOnItemClick setOnItemClick;
    int categoryId = 0;

    public ProductListAdapter(Context context, List<ProductListItems> itemses) {
        this.context = context;
        this.itemsList = itemses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProductListItems item = itemsList.get(position);

        Glide.with(context)
                .load(item.thubnailImage)
                .placeholder(R.drawable.img_loading_placeholder)
                .error(R.drawable.img_no_image)
                .into(holder.imgProduct);

        holder.txtName.setText(item.name);
        holder.txtQty.setText(item.qty + "");

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.qty != 0) {
                    if (item.qty == 2 && categoryId == 14)
                        item.qty = 0;
                    else if (item.qty == 3 && categoryId == 25)
                        item.qty = 0;
                    else if (item.qty == 4 && categoryId == 9)
                        item.qty = 0;
                    else if (item.qty == 5 && (categoryId == 19 || categoryId == 20 || categoryId == 21))
                        item.qty = 0;
                    else if (item.qty == 10 && categoryId == 18)
                        item.qty = 0;
                    else if (item.qty == 1 && categoryId == 30)
                        item.qty = 0;
                    else item.qty = item.qty - 1;
                    notifyItemChanged(position);
                }
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.qty == 0 && categoryId == 14)
                    item.qty = item.qty + 2;
                else if (item.qty == 0 && categoryId == 25)
                    item.qty = item.qty + 3;
                else if (item.qty == 0 && categoryId == 9)
                    item.qty = item.qty + 4;
                else if (item.qty == 0 && (categoryId == 19 || categoryId == 20 || categoryId == 21))
                    item.qty = item.qty + 5;
                else if (item.qty == 0 && categoryId == 18)
                    item.qty = item.qty + 10;
                else if (item.qty == 0 && categoryId == 30)
                    item.qty = item.qty + 1;
                else item.qty = item.qty + 1;
                notifyItemChanged(position);
            }
        });

        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View viewMain;
        TextView txtName, txtQty;
        ImageView btnPlus, btnMinus, imgProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            viewMain = itemView.findViewById(R.id.view_product_list_a_main);
            txtName = itemView.findViewById(R.id.txt_product_list_a_name);
            txtQty = itemView.findViewById(R.id.txt_product_list_a_qty);
            btnPlus = itemView.findViewById(R.id.img_product_list_a_plus);
            btnMinus = itemView.findViewById(R.id.img_product_list_a_minus);
            imgProduct = itemView.findViewById(R.id.img_product_list_a_image);
        }
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void showFullImage(int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_show_full_product_image, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        ImageView imgFullImage = dialogView.findViewById(R.id.img_product_image_full);
        Glide.with(context)
                .load(itemsList.get(position).image)
                .placeholder(R.drawable.img_loading_placeholder)
                .error(R.drawable.img_no_image)
                .into(imgFullImage);

        alertDialog.show();
    }

    boolean isOdd(int val) {
        return (val & 0x01) != 0;
    }

    public void SetOnItemClickListner(SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
    }

    public interface SetOnItemClick {
        public void onItemClick(int position);

        public void onItemRemoved();
    }
}
