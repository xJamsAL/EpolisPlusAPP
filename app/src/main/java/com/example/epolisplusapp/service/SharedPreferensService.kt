package com.example.epolisplusapp.service

import android.content.Context
import android.content.SharedPreferences
import com.example.epolisplusapp.interfaces.IConstanta

class PreferenceService private constructor(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: PreferenceService? = null

        fun getInstance(context: Context): PreferenceService {
            return instance ?: synchronized(this) {
                instance ?: PreferenceService(context).also { instance = it }
            }
        }
    }

    fun getAccessToken(): String {
        return preferences.getString(IConstanta.USER_ACCESS_TOKEN, "") ?: ""
    }

    fun setAccessToken(token: String) {
        preferences.edit().putString(IConstanta.USER_ACCESS_TOKEN, token).apply()
    }

}
