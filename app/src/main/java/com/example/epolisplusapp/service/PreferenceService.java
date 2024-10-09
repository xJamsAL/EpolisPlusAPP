package com.example.epolisplusapp.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.epolisplusapp.util.IConstanta;

public class PreferenceService {
    static PreferenceService controller;
    static SharedPreferences preferences;

    private PreferenceService(Context context) {
        preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }


    public static PreferenceService getInstance(Context context) {
        if (controller == null) {
            controller = new PreferenceService(context);
        }
        return controller;
    }

    public String getAccesToken() {
        return preferences.getString(IConstanta.USER_ACCESS_TOKEN, "");
    }

}
