package com.example.epolisplusapp.ui.dopservice

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.api.MainApi

class DopFormsViewModelFactory(
    private val apiService: MainApi.ApiService,
    private val sharedPreferences: SharedPreferences,

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DopFormsViewModel::class.java)) {
            return DopFormsViewModel(apiService, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
