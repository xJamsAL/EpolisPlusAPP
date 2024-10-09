package com.example.epolisplusapp.ui.dopservice

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.cabinet.AddCarRequest
import com.example.epolisplusapp.models.profile.CarInfo
import com.example.epolisplusapp.service.PreferenceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DopFormsViewModel(
    private val apiService: MainApi.ApiService,
    private val sharedPreferences: SharedPreferences,
) : ViewModel(){

    val carInfoLiveData = MutableLiveData<List<CarInfo>>()
    val errorLiveData = MutableLiveData<String>()
    val successLiveData = MutableLiveData<Boolean>()



    fun sendCarData2(addCarRequest: AddCarRequest) {
        Log.d("1234", "sendCarData2 called with: $addCarRequest")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //     val accessToken = sharedPreferences.getString("access_token", null)
                PreferenceService.getInstance(this).getAccesToken()
                if (accessToken == null) {
                    Log.e("1234", "Access token is null")
                    errorLiveData.postValue("Токен недействителен")
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
                        errorLiveData.postValue("Ошибка: ${addCarResponse.message}")
                    }
                }
            } catch (e: HttpException) {
                Log.e("1234", "HttpException: ${e.message()}")
                errorLiveData.postValue("Ошибка сети: ${e.message()}")
            } catch (e: Exception) {
                Log.e("1234", "Exception: ${e.message}")
                errorLiveData.postValue("Произошла ошибка: ${e.message}")
            }
        }
    }


    fun loadCarInfo(accessToken: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUserProfile("Bearer $accessToken")
                val carInfo = response.response.car_info
                carInfoLiveData.postValue(carInfo)
            } catch (e: HttpException) {

                errorLiveData.postValue("Error: ${e.message()}")
            } catch (e: Exception) {
                errorLiveData.postValue("Error: ${e.localizedMessage}")
            }
        }
    }
}
