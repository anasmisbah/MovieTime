package com.example.movietime.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingPreference {
    public static final String NOTIF_DAILY = "NOTIF_DAILY";
    public static final String NOTIF_RELEASE = "NOTIF_RELEASE";
    private SharedPreferences sharedPreferences;

    public SettingPreference(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setNotifDaily(Boolean notifDaily){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NOTIF_DAILY,notifDaily);
        editor.apply();
    }

    public Boolean getDaily(){
        return sharedPreferences.getBoolean(NOTIF_DAILY,false);
    }

    public void setNotifRelease(Boolean notifRelease){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NOTIF_RELEASE,notifRelease);
        editor.apply();
    }

    public Boolean getRelease(){
        return sharedPreferences.getBoolean(NOTIF_RELEASE,false);
    }

}
