package com.flitzen.rmapp.AdaptersAndItems;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.rmapp.R;
import com.flitzen.rmapp.activities.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    List<OrderListItems> itemsList = new ArrayList<>();
    Context context;
    SetOnItemClick setOnItemClick;

    public OrderListAdapter(Context context, List<OrderListItems> itemses) {
        this.context = context;
        this.itemsList = itemses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OrderListItems item = itemsList.get(position);
        holder.txtOrderNo.setText("#" + item.oNumber);
        holder.txtDate.setText(item.oDate);
        holder.txtInvoiceTo.setText(item.customerName);

        holder.btnViewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url = item.pdfUrl;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);*/
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(WebViewActivity.PRAM_TITLE, "#" + item.oNumber);
                intent.putExtra(WebViewActivity.PRAM_URL, item.webUrl);
                intent.putExtra(WebViewActivity.PRAM_PDF_URL, item.pdfUrl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View viewMain;
        TextView txtOrderNo, txtDate, txtInvoiceTo;
        CardView btnViewPdf;

        public ViewHolder(View itemView) {
            super(itemView);
            viewMain = itemView.findViewById(R.id.view_order_list_a_main);
            txtOrderNo = itemView.findViewById(R.id.txt_order_list_a_number);
            txtDate = itemView.findViewById(R.id.txt_order_list_a_date);
            txtInvoiceTo = itemView.findViewById(R.id.txt_order_list_a_invoice_to);
            btnViewPdf = itemView.findViewById(R.id.btn_order_list_a_view_pdf);
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

        public void onItemRemoved();
    }
}
