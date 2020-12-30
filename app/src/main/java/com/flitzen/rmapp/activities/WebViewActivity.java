package com.flitzen.rmapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    public static String PRAM_TITLE = "title";
    public static String PRAM_URL = "url";
    public static String PRAM_PDF_URL = "pdfUrl";

    @BindView(R.id.txt_webview_title)
    TextView txtTitle;
    @BindView(R.id.webview)
    WebView webView;

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        onActivityStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        progressBar = new ProgressDialog(WebViewActivity.this);

        txtTitle.setText(getIntent().getStringExtra(PRAM_TITLE));

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.show();
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.hide();
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.hide();
                invalidateOptionsMenu();
            }
        });

        if (Helper.isInternetAvailable(WebViewActivity.this)) {
            webView.loadUrl(getIntent().getStringExtra(PRAM_URL));
        } else {
            Toast.makeText(WebViewActivity.this, "Check your internet connection and try again!", Toast.LENGTH_SHORT).show();
            finish();
        }

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    public void downloadPDF(View view){
        String url = getIntent().getStringExtra(PRAM_PDF_URL);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void onBackPress(View view) {
        finish();
    }
}