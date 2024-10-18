package com.example.epolisplusapp.ui.dopservice

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.error_models.ApiErrorMessage
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.NetworkFailure
import com.example.epolisplusapp.models.error_models.TokenFailure
import com.example.epolisplusapp.models.profile.CarInfo
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DopFormsViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService,

) : ViewModel() {

    val carInfoLiveData = MutableLiveData<List<CarInfo>>()
    val errorLiveData = MutableLiveData<Failure>()
    val successLiveData = MutableLiveData<Boolean>()



    companion object {
        fun create(context: Context):DopFormsViewModel {
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            return DopFormsViewModel(apiService, preferenceService)
        }
    }

    fun sendCarData2(addCarRequest: AddCarRequest) {
        Log.d("1234", "sendCarData2 called with: $addCarRequest")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val accessToken = preferenceService.getAccessToken()
                if (accessToken.isEmpty()) {
                    Log.e("1234", "Access token is empty")
                    errorLiveData.postValue(TokenFailure())
                    return@launch
                }

                Log.d("1234", "Sending request to add car with access token")
                val addCarResponse = apiService.addCar("Bearer $accessToken", addCarRequest)

                withContext(Dispatchers.Main) {
                    Log.d("1234", "Received response: $addCarResponse")
                    if (addCarResponse.status == 200) {
                        Log.d("1234", "Car added successfully")
                        successLiveData.postValue(true)
                    } else {
                        Log.e("1234", "Error adding car: ${addCarResponse.message}")
                        errorLiveData.postValue(ApiErrorMessage(addCarResponse.message))
                    }
                }
            } catch (e: HttpException) {
                Log.e("1234", "HttpException: ${e.message()}")
                errorLiveData.postValue(NetworkFailure(e.message()))
            } catch (e: Exception) {
                Log.e("1234", "Exception: ${e.message}")
                errorLiveData.postValue(GenericFailure())
            }
        }
    }


    fun loadCarInfo() {
        viewModelScope.launch {
            try {
                val token = preferenceService.getAccessToken()
                val response = apiService.getUserProfile("Bearer $token")
                val carInfo = response.response.car_info
                carInfoLiveData.postValue(carInfo)
            } catch (e: HttpException) {
                errorLiveData.postValue(NetworkFailure(e.message()))
            } catch (e: Exception) {
                errorLiveData.postValue(GenericFailure())
            }
        }
    }
}
