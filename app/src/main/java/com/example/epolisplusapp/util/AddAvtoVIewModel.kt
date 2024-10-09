package com.example.epolisplusapp.util

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.cabinet.AddCarRequest
import com.example.epolisplusapp.models.cabinet.CheckCarRequest
import com.example.epolisplusapp.ui.dopservice.CarDataListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddAvtoViewModel(
    private val apiService: MainApi.ApiService,
    private val sharedPreferences: SharedPreferences,
    private var listener: CarDataListener? = null
) : ViewModel() {

    val checkCarRequestLiveData = MutableLiveData<CheckCarRequest>()
    val addCarRequestLiveData = MutableLiveData<AddCarRequest>()
    val successMessageLiveData = MutableLiveData<String>()
    val errorMessageLiveData = MutableLiveData<String>()
    val carDataLiveData = MutableLiveData<CheckCarResponse?>()
    val isContainerVisible = MutableLiveData<Boolean>(false)
    val isSecondContainerVisible = MutableLiveData<Boolean>(false)


    fun sendCarData(techSeriya: String, techNomer: String, avtoRegion: String, avtoNomer: String) {
        Log.d("1234", "sendCarData вызван с параметрами: techSeriya=$techSeriya, techNomer=$techNomer, avtoRegion=$avtoRegion, avtoNomer=$avtoNomer")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken == null) {
                    errorMessageLiveData.postValue("Токен недействителен")
                    return@launch
                }

                val formattedTechNomer = avtoNomer.replace(" ", "")
                val request = CheckCarRequest(
                    govNumber = "$avtoRegion$formattedTechNomer",
                    techPassportNumber = techNomer,
                    techPassportSeria = techSeriya
                )
                checkCarRequestLiveData.postValue(request)
                val response: CheckCarResponse = apiService.checkCar("Bearer $accessToken", request)
                if (response.response.ERROR == "0") {
                    val carData = response.response
                    Log.d("1234", "Получены данные автомобиля: $carData")
                    val addCarRequest = AddCarRequest(
                        BODY_NUMBER = carData.BODY_NUMBER,
                        ENGINE_NUMBER = carData.ENGINE_NUMBER,
                        FIRST_NAME = carData.FIRST_NAME,
                        FY = carData.FY,
                        GOV_NUMBER = "$avtoRegion$formattedTechNomer",
                        INN = carData.INN,
                        ISSUE_YEAR = carData.ISSUE_YEAR,
                        LAST_NAME = carData.LAST_NAME,
                        MARKA_ID = carData.MARKA_ID,
                        MIDDLE_NAME = carData.MIDDLE_NAME,
                        MODEL_ID = carData.MODEL_ID,
                        MODEL_NAME = carData.MODEL_NAME,
                        ORGNAME = carData.ORGNAME,
                        PINFL = carData.PINFL,
                        TECH_NUMBER = techNomer,
                        TECH_PASSPORT_ISSUE_DATE = carData.TECH_PASSPORT_ISSUE_DATE,
                        TECH_SERIYA = techSeriya,
                        USE_TERRITORY = carData.USE_TERRITORY,
                        VEHICLE_TYPE_ID = carData.VEHICLE_TYPE_ID
                    )

                    Log.d("1234", "Обновление addCarRequestLiveData: $addCarRequest")
                    addCarRequestLiveData.postValue(addCarRequest)
                    carDataLiveData.postValue(response)
                    successMessageLiveData.postValue("Данные успешно получены")
                    Log.d("1234", "Передаем данные слушателю: $addCarRequest")
                    listener?.onCarDataReceived(addCarRequest)

                } else {
                    errorMessageLiveData.postValue("Ошибка: ${response.message}")
                }
            } catch (e: HttpException) {
                errorMessageLiveData.postValue("Ошибка сети: ${e.message}")
            } catch (e: Exception) {
                errorMessageLiveData.postValue("Произошла ошибка: ${e.message}")
            }
        }
    }

    fun MutableLiveData<CheckCarResponse>.postValue(response: CheckCarResponse) {
        this.postValue(response)
    }
    fun resetData() {
        carDataLiveData.postValue(null)
        isContainerVisible.postValue(false)
        isSecondContainerVisible.postValue(false)
    }

}
