package com.flitzen.rmapp.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.flitzen.rmapp.R;

/**
 * Created by Vivek Aghera on 03/10/2017.
 */

public class ProgressDialog {

    android.app.AlertDialog alertDialog;

    String message = null;
    Context context;
    boolean onOutterTouch = false;
    private TextView txtMessage;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCancelOnOuterTouch(boolean value) {
        this.onOutterTouch = value;
    }

    public void show() {
        if (alertDialog == null) {
            LayoutInflater localView = LayoutInflater.from(context);
            View dialogView = localView.inflate(R.layout.dialog_progress_bar, null);

            final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            alertDialogBuilder.setView(dialogView);
            alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(onOutterTouch);

            txtMessage = (TextView) dialogView.findViewById(R.id.txt_custom_progress_bar_title);
            if (message == null)
                txtMessage.setVisibility(View.GONE);
            else {
                txtMessage.setText(message);
                txtMessage.setVisibility(View.VISIBLE);
            }

            alertDialog.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            alertDialog.getWindow().setAttributes(lp);
        } else if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void hide() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    public boolean isShowing() {
        if (alertDialog != null)
            return alertDialog.isShowing();
        return false;
    }
}
