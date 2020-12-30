package com.flitzen.rmapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.flitzen.rmapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderConfirmActivity extends BaseActivity {

    public static String PRAM_ORDER_NO = "order_no";

    @BindView(R.id.txt_order_cofirm_number)
    TextView txtOrderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        String orderNo = getIntent().getStringExtra(PRAM_ORDER_NO);
        txtOrderNumber.setText(Html.fromHtml("Order Number Is: <b>" + orderNo + "</b>"));
    }

    public void onGotoHome(View view) {
        Intent intent = new Intent(OrderConfirmActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onGotoHome(null);
    }
}