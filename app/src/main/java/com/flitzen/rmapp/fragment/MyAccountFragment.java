package com.flitzen.rmapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.activities.BaseActivity;
import com.flitzen.rmapp.activities.CartActivity;
import com.flitzen.rmapp.activities.FavoriteActivity;
import com.flitzen.rmapp.activities.HomeActivity;
import com.flitzen.rmapp.activities.MyProfileActivity;
import com.flitzen.rmapp.activities.OrderHistoryActivity;
import com.flitzen.rmapp.activities.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAccountFragment extends Fragment {

    private static int REQUEST_FAVORITE = 201;
    private static int REQUEST_CART = 202;

    @BindView(R.id.btn_my_account_back)
    View btnBack;
    @BindView(R.id.txt_my_account_name)
    TextView txtName;

    @BindView(R.id.btn_my_account_profile)
    View btnProfile;
    @BindView(R.id.btn_my_account_cart)
    View btnCart;
    @BindView(R.id.btn_my_account_order_history)
    View btnHistory;
    @BindView(R.id.btn_my_account_favorites)
    View btnFavorite;
    @BindView(R.id.btn_my_account_address)
    View btnAddress;
    @BindView(R.id.btn_my_account_logout)
    View btnLogout;

    BaseActivity baseActivity;

    public MyAccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, view);
        baseActivity = ((BaseActivity) getActivity());

        txtName.setText(baseActivity.sharedPreferences.getString(SharePref.USERNAME, ""));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).onBackPress();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CartActivity.class),REQUEST_CART);
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), FavoriteActivity.class), REQUEST_FAVORITE);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharePref.Clear(baseActivity.sharedPreferences);
                                Intent intent = new Intent(getActivity(), SplashActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("Cancel", null).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_FAVORITE == requestCode && resultCode == Activity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putString(ProductDetailsFragment.PRAM_TITLE, data.getStringExtra(FavoriteActivity.PRAM_CAT_NAME));
            bundle.putInt(ProductDetailsFragment.PRAM_CAT_ID, data.getIntExtra(FavoriteActivity.PRAM_CAT_ID, 0));
            Fragment fragment = new ProductDetailsFragment();
            fragment.setArguments(bundle);
            ((HomeActivity) getActivity()).replaceFragment(fragment);
        }
        if (requestCode == REQUEST_CART && resultCode == CartActivity.OPEN_PRODUCT_DETAILS) {
            Bundle bundle = new Bundle();
            bundle.putString(ProductDetailsFragment.PRAM_TITLE, data.getStringExtra(ProductDetailsFragment.PRAM_TITLE));
            bundle.putInt(ProductDetailsFragment.PRAM_CAT_ID, data.getIntExtra(ProductDetailsFragment.PRAM_CAT_ID,0));
            Fragment fragment = new ProductDetailsFragment();
            fragment.setArguments(bundle);
            ((HomeActivity) getActivity()).replaceFragment(fragment);
        }
    }
}