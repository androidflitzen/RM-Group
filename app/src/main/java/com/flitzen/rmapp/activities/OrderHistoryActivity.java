package com.flitzen.rmapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.AdaptersAndItems.OrderListAdapter;
import com.flitzen.rmapp.AdaptersAndItems.OrderListItems;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends BaseActivity {

    @BindView(R.id.recyclerview_order_list)
    RecyclerView recyclerView;
    @BindView(R.id.view_order_list_empty)
    View viewEmpty;

    ArrayList<OrderListItems> arrayList = new ArrayList<>();
    private OrderListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
        mAdapter = new OrderListAdapter(OrderHistoryActivity.this, arrayList);
        recyclerView.setAdapter(mAdapter);

        getOrderData();
    }

    public void getOrderData() {
        String url = String.format(API.OrderList, sharedPreferences.getString(SharePref.UID, ""));
        ProgressDialog progressDialog = new ProgressDialog(OrderHistoryActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        requestAPI(url, new RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        arrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            OrderListItems item = new OrderListItems();
                            item.setoId(jObj.getInt("invoice_id"));
                            item.setoNumber(jObj.getString("unique_id"));
                            item.setoDate(jObj.getString("invoice_dt"));
                            item.setCustomerName(jObj.getString("invoice_to"));
                            item.setSalesPerson(jObj.getString("sales_person"));
                            item.setPdfUrl(jObj.getString("pdf_url"));
                            item.setWebUrl(jObj.getString("web_url"));
                            arrayList.add(item);
                        }

                        if (arrayList.size() == 0) {
                            viewEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        mAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(OrderHistoryActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                        viewEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(OrderHistoryActivity.this, "Failed to get your order(s), Please try again", Toast.LENGTH_SHORT).show();
                viewEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    public void onBackPress(View view) {
        finish();
    }
}