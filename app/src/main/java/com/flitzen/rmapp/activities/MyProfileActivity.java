package com.flitzen.rmapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileActivity extends BaseActivity {

    @BindView(R.id.txt_my_profile_name)
    TextView txtName;
    @BindView(R.id.txt_my_profile_mobile)
    TextView txtMobile;
    @BindView(R.id.txt_my_profile_email)
    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        getProfileInformation();
    }

    public void getProfileInformation() {
        ProgressDialog progressDialog = new ProgressDialog(MyProfileActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = String.format(API.ProfileInfo, sharedPreferences.getString(SharePref.UID, ""));
        requestAPI(url, new RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        txtName.setText(jsonObject.getString("customer_name"));
                        txtMobile.setText(jsonObject.getString("phone_number"));
                        txtEmail.setText(jsonObject.getString("email"));
                    } else {
                        Toast.makeText(MyProfileActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(MyProfileActivity.this, "Failed to get information, try again!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void onEditProfileClick(View view) {
        Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
        intent.putExtra(EditProfileActivity.PRAM_PHONE, txtMobile.getText().toString().trim());
        intent.putExtra(EditProfileActivity.PRAM_NAME, txtName.getText().toString().trim());
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101)
            getProfileInformation();
    }

    public void onBackPress(View view) {
        finish();
    }
}