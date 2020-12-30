package com.flitzen.rmapp.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Vivek Aghera on 16/02/2017.
 */

public class Helper {

    public static final String defaultFormate = "yyyy-MM-dd HH:mm:ss";
    public static final String defaultDateFormate = "yyyy-MM-dd";
    public static final String defaultTimeFormate = "HH:mm:ss";
    public static String AMOUNT_FORMAT = "#,##,##,###.##";
    public static String FLIGHT_N_FORMAT = "#,##,##,###";
    public static String NUMBER_FORMAT = "##.##";

    public static void LOG(String key, String value) {
        Log.e(key, "-" + value);
    }

    public static String getCurrentDateTime(String formate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                formate, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null)
            return true;
        value = value.trim();
        if (value.isEmpty())
            return true;
        if (value.equals("null"))
            return true;
        return false;
    }

    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public final static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidNumber(String target) {
        Pattern NUMBER
                = Pattern.compile(
                "(\\+[0-9]+[\\- \\.]*)?"
                        + "(\\([0-9]+\\)[\\- \\.]*)?"
                        + "([0-9][0-9\\- \\.]+[0-9])");
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }

}

