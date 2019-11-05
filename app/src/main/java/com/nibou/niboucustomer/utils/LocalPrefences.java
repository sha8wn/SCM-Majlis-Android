package com.nibou.niboucustomer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.HashSet;

public class LocalPrefences {
    private static LocalPrefences mInstance = null;

    interface PREF_VAR {
        String SHER_PREF = "scm_db";
    }

    private LocalPrefences() {
    }

    public static LocalPrefences getInstance() {
        if (mInstance == null) {
            mInstance = new LocalPrefences();
        }
        return mInstance;
    }


    public boolean isUserLogin(Context context) {
        if (getString(context, AppConstant.USER_ID) != null && !getString(context, AppConstant.USER_ID).isEmpty()) {
            return true;
        }
        return false;
    }

    public void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_VAR.SHER_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(Context context, String key) {
        SharedPreferences spref = context.getSharedPreferences(PREF_VAR.SHER_PREF, Context.MODE_PRIVATE);
        return spref.getString(key, null);
    }

    public void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_VAR.SHER_PREF, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(Context context, String key) {
        SharedPreferences spref = context.getSharedPreferences(PREF_VAR.SHER_PREF, Context.MODE_PRIVATE);
        return spref.getInt(key, 0);
    }

    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_VAR.SHER_PREF, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(Context context, String key) {
        SharedPreferences spref = context.getSharedPreferences(PREF_VAR.SHER_PREF, Context.MODE_PRIVATE);
        return spref.getBoolean(key, false);
    }

    public void clearSharePreference(Context context) {
        context.getSharedPreferences(PREF_VAR.SHER_PREF, 0).edit().clear().apply();
    }
}
