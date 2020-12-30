package com.flitzen.rmapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.AdaptersAndItems.SilverSubCategoryAdapter;
import com.flitzen.rmapp.AdaptersAndItems.SilverSubCategoryItems;
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

public class FavoriteActivity extends BaseActivity {

    public static String PRAM_CAT_NAME = "catName";
    public static String PRAM_CAT_ID = "catId";

    @BindView(R.id.recyclerview_favorite_list)
    RecyclerView recyclerView;
    @BindView(R.id.view_favorite_empty)
    View viewEmpty;

    ArrayList<SilverSubCategoryItems> arrayList = new ArrayList<SilverSubCategoryItems>();
    private SilverSubCategoryAdapter mAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        recyclerView.setLayoutManager(new GridLayoutManager(FavoriteActivity.this, 2));
        mAdaper = new SilverSubCategoryAdapter(FavoriteActivity.this, arrayList);
        recyclerView.setAdapter(mAdaper);

        getFavoriteList();

        mAdaper.SetOnItemClickListner(new SilverSubCategoryAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = getIntent();
                intent.putExtra(PRAM_CAT_ID, arrayList.get(position).id);
                intent.putExtra(PRAM_CAT_NAME, arrayList.get(position).name);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemFavoriteClick(int position) {
                favoriteClick(position);
            }
        });
    }

    public void getFavoriteList() {
        String url = String.format(API.FavoriteList,
                sharedPreferences.getString(SharePref.UID, ""));

        ProgressDialog progressDialog = new ProgressDialog(FavoriteActivity.this);
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
                        if (jsonArray.length() == 0) {
                            viewEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            viewEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                SilverSubCategoryItems item = new SilverSubCategoryItems();
                                item.setId(jObj.getInt("c_id"));
                                item.setMainCategoryId(0);
                                item.setName(jObj.getString("c_name"));
                                item.setImage(jObj.getString("c_image"));
                                item.setFavorite(true);
                                item.setSetFavorite(true);
                                item.setParentCategoryId(0);
                                arrayList.add(item);
                            }
                        }
                        mAdaper.notifyDataSetChanged();
                    } else {
                        Toast toast = Toast.makeText(FavoriteActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast toast = Toast.makeText(FavoriteActivity.this,"Failed to get favorite item(s), try again!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                finish();
            }
        });
    }

    public void favoriteClick(int position) {
        String url = String.format(API.FavoriteItem,
                sharedPreferences.getString(SharePref.UID, ""),
                arrayList.get(position).id);

        requestAPI(url, new BaseActivity.RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(FavoriteActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    mAdaper.notifyItemRemoved(position);
                    if (arrayList.size() == 0) {
                        viewEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(FavoriteActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPress(View view) {
        finish();
    }
}