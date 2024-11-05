package com.example.epolisplusapp.module.module_add_avto

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.interfaces.ICarDataListener
import com.example.epolisplusapp.models.BaseApiResponse
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.cabinet.request.CheckCarRequest
import com.example.epolisplusapp.models.cabinet.response.AddUserCarResponse
import com.example.epolisplusapp.models.error_models.ApiErrorMessage
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.NetworkFailure
import com.example.epolisplusapp.models.error_models.TokenFailure
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddAvtoViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService,
    private val listener: ICarDataListener

) : ViewModel() {

    companion object {
        fun create(context: Context, listener: ICarDataListener): AddAvtoViewModel {
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            return AddAvtoViewModel(apiService, preferenceService, listener)
        }
    }

    val addCarRequestLiveData = MutableLiveData<AddCarRequest>()

    val errorMessageLiveData = MutableLiveData<Failure>()
    private val carDataLiveData = MutableLiveData<BaseApiResponse<AddUserCarResponse>?>()

    fun sendCarData(techSeriya: String, techNomer: String, avtoRegion: String, avtoNomer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val accessToken = preferenceService.getAccessToken()
                if (accessToken.isEmpty()) {
                    errorMessageLiveData.postValue(TokenFailure())
                    return@launch
                }

                val formattedTechNomer = avtoNomer.replace(" ", "")
                val request = CheckCarRequest(
                    govNumber = "$avtoRegion$formattedTechNomer",
                    techPassportNumber = techNomer,
                    techPassportSeria = techSeriya
                )

                val response = apiService.checkCar("Bearer $accessToken", request)
                if (response.response.ERROR == "0") {
                    val carData = response.response
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
                    listener.onCarDataReceived(addCarRequest)
                    addCarRequestLiveData.postValue(addCarRequest)

                    Log.d("1234", "pass data: $addCarRequest")
                } else {
                    errorMessageLiveData.postValue(ApiErrorMessage(response.message))
                }
            } catch (e: HttpException) {
                errorMessageLiveData.postValue(NetworkFailure(e.message()))
            } catch (e: Exception) {
                errorMessageLiveData.postValue(GenericFailure())
            }
        }
    }
    fun resetData() {
        carDataLiveData.postValue(null)
    }

}
