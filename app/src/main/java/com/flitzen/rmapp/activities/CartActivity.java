package com.flitzen.rmapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.AdaptersAndItems.CartAdapter;
import com.flitzen.rmapp.AdaptersAndItems.CartItems;
import com.flitzen.rmapp.AdaptersAndItems.CartSubItems;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.fragment.ProductDetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseActivity {

    public static int OPEN_PRODUCT_DETAILS = 101;
    @BindView(R.id.recyclerview_cart)
    RecyclerView recyclerView;
    @BindView(R.id.view_cart_data)
    View viewData;
    @BindView(R.id.view_cart_empty)
    View viewEmpty;

    ArrayList<CartItems> arrayList = new ArrayList<CartItems>();
    private CartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        mAdapter = new CartAdapter(CartActivity.this, arrayList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListner(new CartAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = getIntent();
                intent.putExtra(ProductDetailsFragment.PRAM_TITLE, arrayList.get(position).categoryName);
                intent.putExtra(ProductDetailsFragment.PRAM_CAT_ID, arrayList.get(position).categoryId);
                setResult(OPEN_PRODUCT_DETAILS, intent);
                finish();
            }

            @Override
            public void onItemRemoved(int position) {
                removeItem(position);
            }
        });
        getCart();
    }

    public void getCart() {

        String url = String.format(API.CartInfo, sharedPreferences.getString(SharePref.UID, ""));
        ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        requestAPI(url, new RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        arrayList.clear();
                        if (jsonArray.length() == 0) {
                            viewEmpty.setVisibility(View.VISIBLE);
                            viewData.setVisibility(View.GONE);
                        } else {
                            viewEmpty.setVisibility(View.GONE);
                            viewData.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                CartItems item = new CartItems();
                                item.setCartId(jObj.getInt("cv_id"));
                                item.setCategoryId(jObj.getInt("c_id"));
                                item.setCategoryName(jObj.getString("c_name"));
                                item.setVariation1(jObj.getString("variation_1"));
                                item.setVariation2(jObj.getString("variation_2"));
                                item.setVariation3(jObj.getString("variation_3"));
                                item.setVariation4(jObj.getString("variation_4"));
                                item.setVariation5(jObj.getString("variation_5"));
                                item.setVariation6(jObj.getString("variation_6"));
                                item.setVariation7(jObj.getString("variation_7"));
                                item.setVariation8(jObj.getString("variation_8"));
                                item.setVariationString(jObj.getString("variation_string"));
                                item.setImgUrl(jObj.getString("c_image"));
                                item.setNote(jObj.getString("notes"));

                                JSONArray jArraySubItems = jObj.getJSONArray("products");
                                List<CartSubItems> subItems = new ArrayList<>();
                                for (int j = 0; j < jArraySubItems.length(); j++) {
                                    JSONObject jObjSubItem = jArraySubItems.getJSONObject(j);
                                    CartSubItems subItem = new CartSubItems();
                                    subItem.setCartId(jObjSubItem.getInt("cart_id"));
                                    subItem.setProductId(jObjSubItem.getInt("product_id"));
                                    subItem.setProductName(jObjSubItem.getString("product_name"));
                                    subItem.setProductId(jObjSubItem.getInt("product_unit"));
                                    subItems.add(subItem);
                                }
                                item.setList(subItems);
                                arrayList.add(item);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CartActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(CartActivity.this, "Failed to get cart data, try again!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void removeItem(int position) {
        new AlertDialog.Builder(CartActivity.this)
                .setTitle("Remove")
                .setMessage("Are you sure you want to remove this item from cart?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
                        progressDialog.setMessage("Removing item...");
                        progressDialog.show();
                        String url = String.format(API.RemoveItemFromCart, arrayList.get(position).cartId);
                        requestAPI(url, new RequestApiInterface() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.hide();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                                        Toast.makeText(CartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                        arrayList.remove(position);
                                        mAdapter.notifyItemRemoved(position);
                                        if (arrayList.size() == 0) {
                                            viewEmpty.setVisibility(View.VISIBLE);
                                            viewData.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(CartActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                progressDialog.hide();
                                Toast.makeText(CartActivity.this, "Failed to remove, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();
    }

    public void onReviewOrderClick(View view) {
        //startActivity(new Intent(CartActivity.this, OrderReviewActivity.class));
        new AlertDialog.Builder(CartActivity.this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        placeOrder();
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    public void placeOrder() {

        String url = String.format(API.CheckOut, sharedPreferences.getString(SharePref.UID, ""));
        ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        requestAPI(url, new RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(CartActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        Intent intent = new Intent(CartActivity.this, OrderConfirmActivity.class);
                        intent.putExtra(OrderConfirmActivity.PRAM_ORDER_NO, jsonObject.getString("uu_id"));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(CartActivity.this, "Failed to create order, try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPress(View view) {
        finish();
    }
}