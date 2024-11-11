package com.example.epolisplusapp.ui.dopservice

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.interfaces.ICarDataListener
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.InputEditTextFailure
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance

class DopFormsViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService,
    private val sharedViewModel: DopFormsSharedViewModel
) : ViewModel(), ICarDataListener {

    val errorLiveData = MutableLiveData<Failure>()

    private val _navigateGeneralInfoNext = MutableLiveData<Boolean>()
    val navigateGeneralInfoNext: LiveData<Boolean> get() = _navigateGeneralInfoNext

    private var carData: AddCarRequest? = null
    val carDataFromShareidViewModel: LiveData<AddCarRequest?> = sharedViewModel.getData()



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

    fun clearShareData() {
        sharedViewModel.clearData()
    }


    fun callOnClickBtn() {
        if (carDataFromShareidViewModel.value != null) {
            _navigateGeneralInfoNext.value = true
            Log.d("1234", "uspex")
        } else {
            _navigateGeneralInfoNext.value = false
            errorLiveData.value = InputEditTextFailure()
        }
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



