package com.flitzen.rmapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.fragment.HomeFragment;
import com.flitzen.rmapp.fragment.MyAccountFragment;
import com.flitzen.rmapp.fragment.ProductDetailsFragment;
import com.flitzen.rmapp.fragment.SilverItemsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity {

    public static int REQUEST_CART = 101;

    String activeFragmentName = "";
    private static String apiResponse = "";
    String silverJson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        getCategory();
    }

    public void replaceFragment(Fragment fragment) {
        activeFragmentName = fragment.getClass().getName();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_home, fragment).addToBackStack(null).commit();
    }


    public void onMyAccountClick(View view) {
        //if(MyAccountFragment.class.getName() != activeFragmentName){
        replaceFragment(new MyAccountFragment());
        //}
    }

    public void onHomeClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Fragment fragment=new SilverItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SilverItemsFragment.PRAM_TITLE, "Silver Items");
        bundle.putString(SilverItemsFragment.PRAM_DATA, silverJson);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    public void onCartClick(View view) {
        startActivityForResult(new Intent(HomeActivity.this, CartActivity.class), REQUEST_CART);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CART && resultCode == CartActivity.OPEN_PRODUCT_DETAILS) {
            Bundle bundle = new Bundle();
            bundle.putString(ProductDetailsFragment.PRAM_TITLE, data.getStringExtra(ProductDetailsFragment.PRAM_TITLE));
            bundle.putInt(ProductDetailsFragment.PRAM_CAT_ID, data.getIntExtra(ProductDetailsFragment.PRAM_CAT_ID, 0));
            Fragment fragment = new ProductDetailsFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment);
        }
    }

    @Override
    public void onBackPressed() {
        onBackPress();
    }

    public void onBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            super.onBackPressed();
        else finish();
    }

    public void setCategory() {
        try {
            JSONObject jsonObject = new JSONObject(apiResponse);
            if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                silverJson = jsonArray.getJSONObject(0).getString("children");

                Fragment fragment=new SilverItemsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SilverItemsFragment.PRAM_TITLE, "Silver Items");
                bundle.putString(SilverItemsFragment.PRAM_DATA, silverJson);
                fragment.setArguments(bundle);
                replaceFragment(fragment);


            } else {
                Toast.makeText(HomeActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getCategory() {

        if (!Helper.isNullOrEmpty(apiResponse)) {
            setCategory();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String categoryUrl = String.format(API.CategoryData, sharedPreferences.getString(SharePref.UID, ""));
        requestAPI(categoryUrl, new BaseActivity.RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                apiResponse = response;
                setCategory();
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(HomeActivity.this, "Failed to get data, try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}