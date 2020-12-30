package com.flitzen.rmapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends BaseActivity {

    public static String PRAM_NAME = "c_name";
    public static String PRAM_PHONE = "c_phone";

    @BindView(R.id.input_edit_profile_name)
    TextInputLayout inputName;
    @BindView(R.id.input_edit_profile_phone)
    TextInputLayout inputPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        inputName.getEditText().setText(getIntent().getStringExtra(PRAM_NAME));
        inputPhone.getEditText().setText(getIntent().getStringExtra(PRAM_PHONE));

    }

    public void onUpdateClick(View view) {
        inputName.setErrorEnabled(false);
        inputPhone.setErrorEnabled(false);

        if (inputName.getEditText().getText().toString().trim().isEmpty()) {
            inputName.setErrorEnabled(true);
            inputName.setError("Enter your name");
            return;
        }
        if (inputPhone.getEditText().getText().toString().trim().isEmpty()) {
            inputPhone.setErrorEnabled(true);
            inputPhone.setError("Enter your mobile number");
            return;
        }

        String url = String.format(API.UpdateProfile, sharedPreferences.getString(SharePref.UID, ""),
                API.utf8Value(inputName.getEditText().getText().toString().trim()),
                API.utf8Value(inputPhone.getEditText().getText().toString().trim()));

        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        requestAPI(url, new RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(EditProfileActivity.this, "Failed to update profile update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPress(View view) {
        finish();
    }
}