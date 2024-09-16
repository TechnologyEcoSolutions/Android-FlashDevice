package com.ngb.colorflashscreen.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.color.call.flash.screen.themes.R;


public class App extends Application {
    private static final String FILE_NAME = "FILE_NAME";
    @SuppressLint("StaticFieldLeak")
    private static App instance;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static boolean isCheck;
    private int flagCheck = 1;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isCheck() {
        return isCheck;
    }

    public static void setCheck( boolean Check ) {
        App.isCheck = Check;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.AppTheme);
        context = getApplicationContext();
    }

    public void savePref( String key, String value ) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public String getPref( String key ) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public void saveUri( String key, String value ) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public String getUri( String key ) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    @SuppressLint("CommitPrefEdits")
    public void saveCheck( String key, boolean check ) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        preferences.edit().putBoolean(key, check).apply();
    }

    public boolean getCheck( String key ) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        return preferences.getBoolean(key, isCheck());
    }

    public int getFlagCheck() {
        return flagCheck;
    }

    public void setFlagCheck( int flagCheck ) {
        this.flagCheck = flagCheck;
    }
}
