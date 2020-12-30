package com.flitzen.rmapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.Class.SharePref;

public class BaseActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    public SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //FirebaseApp.initializeApp(this);
    }

    public void onActivityStart() {
        sharedPreferences = SharePref.getSharePref(BaseActivity.this);
    }

    public void requestAPI(String url, RequestApiInterface requestApiInterface) {

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(BaseActivity.this);
        Helper.LOG("Url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Helper.LOG("Response", response);
                        requestApiInterface.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Helper.LOG("error", error.toString());
                requestApiInterface.onError(error);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                API.TIMEOUT_LIMIT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public interface RequestApiInterface {
        void onResponse(String response);

        void onError(VolleyError error);
    }
}

