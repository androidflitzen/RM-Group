package com.flitzen.rmapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.activities.BaseActivity;
import com.flitzen.rmapp.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.btn_category_silver)
    View btnSilver;
    @BindView(R.id.btn_category_gold)
    View btnGold;

    @BindView(R.id.img_home_category_silver)
    ImageView imgSilver;
    @BindView(R.id.img_home_category_gold)
    ImageView imgGold;

    @BindView(R.id.txt_home_category_silver)
    TextView txtSilver;
    @BindView(R.id.txt_home_category_gold)
    TextView txtGold;

    BaseActivity baseActivity;
    String silverJson = "", goldJson = "";
    private static String apiResponse = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        baseActivity = ((BaseActivity) getActivity());

        btnSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(SilverItemsFragment.PRAM_TITLE, "Silver Items");
                bundle.putString(SilverItemsFragment.PRAM_DATA, silverJson);
                Fragment fragment = new SilverItemsFragment();
                fragment.setArguments(bundle);
                ((HomeActivity) getActivity()).replaceFragment(fragment);
            }
        });

        btnGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).replaceFragment(new GoldComingSoonFragment());
            }
        });

        getCategory();


        return view;
    }

    public void getCategory() {

        if (!Helper.isNullOrEmpty(apiResponse)) {
            setCategory();
            return;
        }

        String categoryUrl = String.format(API.CategoryData, baseActivity.sharedPreferences.getString(SharePref.UID, ""));
        baseActivity.requestAPI(categoryUrl, new BaseActivity.RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                apiResponse = response;
                setCategory();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getActivity(), "Failed to get data, try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCategory() {
        try {
            JSONObject jsonObject = new JSONObject(apiResponse);
            if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                Glide.with(getActivity())
                        .load(jsonArray.getJSONObject(0).getString("c_image"))
                        .placeholder(R.drawable.img_loading_placeholder)
                        .into(imgSilver);
                txtSilver.setText(jsonArray.getJSONObject(0).getString("c_name"));
                txtSilver.setTag(jsonArray.getJSONObject(0).getString("c_id"));
                silverJson = jsonArray.getJSONObject(0).getString("children");

                /*Glide.with(getActivity())
                        .load(jsonArray.getJSONObject(0).getString("c_image"))
                        .placeholder(R.drawable.img_loading_placeholder)
                        .into(imgGold);
                txtGold.setText(jsonArray.getJSONObject(0).getString("c_name"));
                txtGold.setTag(jsonArray.getJSONObject(0).getString("c_id"));
                goldJson = jsonArray.getJSONObject(1).getString("children");*/
            } else {
                Toast.makeText(getActivity(), jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}