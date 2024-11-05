package com.example.epolisplusapp.ui.dopservice

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.interfaces.ICarDataListener
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.error_models.ApiErrorMessage
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.NetworkFailure
import com.example.epolisplusapp.models.error_models.TokenFailure
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DopFormsViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService
) : ViewModel(), ICarDataListener {

    val errorLiveData = MutableLiveData<Failure>()

    private val _carDataReceived = MutableLiveData<AddCarRequest>()
    val carDataReceived: LiveData<AddCarRequest> get() = _carDataReceived

    private val _navigateGeneralInfoNext = MutableLiveData(false)
    val navigateGeneralInfoNext: LiveData<Boolean> = _navigateGeneralInfoNext

    private var carData: AddCarRequest? = null

    override fun onCarDataReceived(addCarRequest: AddCarRequest) {
        // Устанавливаем значение `carData` и обновляем LiveData
        carData = addCarRequest
        _carDataReceived.postValue(addCarRequest)  // Это для уведомления об изменениях в Fragment
        Log.d("1234", "Data received in DopFormsViewModel: $addCarRequest")
    }

    fun callOnClickBtn() {
        Log.d("1234", "Данные перед отправкой: $carData")
        if (carData != null) {
            _navigateGeneralInfoNext.postValue(true)
        } else {
            errorLiveData.postValue(GenericFailure())
        }
    }

    fun resetNavigation() {
        _navigateGeneralInfoNext.value = false
    }

    companion object {
        fun create(context: Context): DopFormsViewModel {
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            return DopFormsViewModel(apiService, preferenceService)
        }
    }
}