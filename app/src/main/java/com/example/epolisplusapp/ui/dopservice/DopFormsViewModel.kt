package com.example.epolisplusapp.ui.dopservice

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import kotlin.math.log

class DopFormsViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService,
    private val sharedViewModel: DopFormsSharedViewModel
) : ViewModel(), ICarDataListener {

    val errorLiveData = MutableLiveData<Failure>()

    private val _carDataReceived = MutableLiveData<AddCarRequest?>()
    val carDataReceived: LiveData<AddCarRequest?> get() = _carDataReceived

    private val _navigateGeneralInfoNext = MutableLiveData(false)
    val navigateGeneralInfoNext: LiveData<Boolean> = _navigateGeneralInfoNext

    private var carData: AddCarRequest? = null
    val carDataFromShareidViewModel: LiveData<AddCarRequest> = sharedViewModel.getData()

    override fun onCarDataReceived(addCarRequest: AddCarRequest) {
//
//        this.carData = addCarRequest
////        _carDataReceived.postValue(addCarRequest)
//        //  _carDataReceived.value = addCarRequest
//        _carDataReceived.postValue(addCarRequest)
//
//        Log.d("1234", "Data received in DopFormsViewModel: $carData")
//        Log.d("1234", "Data received in DopFormsViewModel: ${_carDataReceived.value}")
//        observeCarData()
    }




    fun callOnClickBtn() {
        carDataFromShareidViewModel.value?.let {
            Log.d("1234", "data in CallOnClickBtn = $it")
        } ?: Log.d("1234", "ne ne ne")
//        viewModelScope.launch {
//
//            Log.d("1234", "Данные перед отправкой: ${_carDataReceived.value}")
//            Log.d("1234", "Данные перед отправкой: ${carData}")
//            if (_carDataReceived.value != null) {
//                _navigateGeneralInfoNext.postValue(true)
//            } else {
//                errorLiveData.postValue(GenericFailure())
//            }
//        }

    }

    fun resetNavigation() {
        _navigateGeneralInfoNext.value = false
    }

    companion object {
        fun create(context: Context): DopFormsViewModel {
            val sharedViewModel =
                ViewModelProvider(context as FragmentActivity)[DopFormsSharedViewModel::class.java]
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            return DopFormsViewModel(apiService, preferenceService, sharedViewModel)
        }
    }
}



