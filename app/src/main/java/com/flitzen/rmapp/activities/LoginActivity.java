package com.flitzen.rmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_login_username)
    EditText edtUsername;
    @BindView(R.id.edt_login_password)
    EditText edtPassword;
    @BindView(R.id.card_login_signin)
    CardView btnSignIn;
    @BindView(R.id.btn_login_crate_account)
    TextView btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void onSignInClick(View view) {
        if (edtUsername.getText().toString().isEmpty()) {
            edtUsername.setError("Enter username");
            return;
        }
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError("Enter password");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        String url = String.format(API.Login, edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
        requestAPI(url, new RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        sharedPreferences.edit().putString(SharePref.UID, jsonObject.getString("user_id")).apply();
                        sharedPreferences.edit().putString(SharePref.USERNAME, jsonObject.getString("username")).apply();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(LoginActivity.this, "Failed to login, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}