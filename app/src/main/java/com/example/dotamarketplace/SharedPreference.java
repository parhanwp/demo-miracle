package com.example.dotamarketplace;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private Context context;
    private final String PREFS_NAME = "default";
    private SharedPreferences sharedPref;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveInt(String KEY_NAME, int value){
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(KEY_NAME, value);

        editor.commit();
    }

    public int getInt(String KEY_NAME) {
        return sharedPref.getInt(KEY_NAME, 0);
    }

    public void saveString(String KEY_NAME, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_NAME, value);
        editor.commit();
    }

    public String getString(String KEY_NAME) {
        return sharedPref.getString(KEY_NAME, null);
    }
}
