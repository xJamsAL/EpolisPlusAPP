package com.example.epolisplusapp.ui.dopservice

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.InputVehicleInformationFailure
import com.example.epolisplusapp.models.profile.CarInfo
import com.example.epolisplusapp.util.IConstanta.CLIENT_DATA_STEP_ID
import com.example.epolisplusapp.util.IConstanta.PAYMENT_STEP_ID

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

    private lateinit var selectedCar: LiveData<CarInfo>
    val errorMessage = MutableLiveData<Failure>()
    val openStepId = MutableLiveData<Int>()
    val successLiveData = MutableLiveData<Boolean>()

    fun onCallNextBtn() {
        if (selectedCar.value != null) {
            openStepId.postValue(CLIENT_DATA_STEP_ID)
        } else {
            errorMessage.postValue(InputVehicleInformationFailure())
        }
        /*    Toast.makeText(
                requireContext(),
                "Выберите элемент перед продолжением",
                Toast.LENGTH_SHORT*/

    }

    fun onCallBackBtn() {

    }

    fun onCallNextBtnClientData() {
        if (true) {
            if (true) if (true) {
                openStepId.postValue(PAYMENT_STEP_ID)
            } else {
                errorMessage.postValue(InputVehicleInformationFailure())

            } else {
                errorMessage.postValue(InputVehicleInformationFailure())
            }
        } else {
            errorMessage.postValue(InputVehicleInformationFailure())

        }


    }

}
