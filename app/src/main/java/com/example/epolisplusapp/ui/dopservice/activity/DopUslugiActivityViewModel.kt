package com.example.epolisplusapp.ui.dopservice.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.R
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.dopuslugi.LitroCalculatorItems
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.NetworkFailure
import com.example.epolisplusapp.models.error_models.TokenFailure
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.util.CommonUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Locale

class DopUslugiActivityViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService
) : ViewModel() {

    val errorMessageLiveData = MutableLiveData<Failure>()
    val progressBarVisible = MutableLiveData<Boolean>()
    val dopListLiveData = MutableLiveData<List<LitroCalculatorItems>>()
    val discountDataLiveData = MutableLiveData<Pair<Int, Int>>()
    val discountTitleLiveData = MutableLiveData<String>()
    val discountBodyLiveData = MutableLiveData<String>()
    val totalSumLiveData = MutableLiveData<String>()
    val discountLiveData = MutableLiveData<String>()
    val finalSumLiveData = MutableLiveData<String>()

    val isItemSelectedLiveData = MutableLiveData<Boolean>()

    fun checkItems(items: Int?) {
        isItemSelectedLiveData.value = items != null && items > 0

    }

    fun fetcLitroList() {
        viewModelScope.launch {
            progressBarVisible.value = true
            try {
                val accessToken = preferenceService.getAccessToken()
                if (accessToken.isNotEmpty()) {
                    val response = apiService.litroCalculator("Bearer $accessToken")
                    dopListLiveData.value = response.response.risk
                    discountDataLiveData.value = Pair(
                        response.response.discount_percent,
                        response.response.discount_length
                    )
                    discountTitleLiveData.value = response.response.discount_title
                    discountBodyLiveData.value = response.response.discount_body
                    Log.d("1234", "${discountDataLiveData.value}")
                } else {
                    errorMessageLiveData.value = TokenFailure()
                }
            } catch (e: HttpException) {
                errorMessageLiveData.value = NetworkFailure(e.message())
            } catch (e: Exception) {
                errorMessageLiveData.value = GenericFailure()
            } finally {
                progressBarVisible.value = false
            }

        }
    }
    companion object {
        fun create(context: Context): DopUslugiActivityViewModel {
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            return DopUslugiActivityViewModel(apiService, preferenceService)
        }
    }
}