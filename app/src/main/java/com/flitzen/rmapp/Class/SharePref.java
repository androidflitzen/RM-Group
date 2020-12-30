package com.flitzen.rmapp.Class;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    public static String SharePrefName = "RUNNING_SERVICE_DETAILS";

    public static String UID = "uid";//user id
    public static String USERNAME = "username";//user id

    public static SharedPreferences getSharePref(Context context) {
        return context.getSharedPreferences(SharePrefName, Context.MODE_PRIVATE);
    }

    public static void Clear(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
