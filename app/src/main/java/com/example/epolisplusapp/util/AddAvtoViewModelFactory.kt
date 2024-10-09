package com.example.epolisplusapp.util

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.ui.dopservice.CarDataListener

class AddAvtoViewModelFactory(
    private val apiService: MainApi.ApiService,
    private val sharedPreferences: SharedPreferences,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAvtoViewModel::class.java)) {
            return AddAvtoViewModel(apiService, sharedPreferences ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
