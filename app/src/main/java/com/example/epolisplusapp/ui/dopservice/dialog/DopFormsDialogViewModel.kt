package com.example.epolisplusapp.ui.dopservice.dialog

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.interfaces.ICarDataListener
import com.example.epolisplusapp.models.TestDataClass
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.InputEditTextFailure
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.ui.dopservice.DopFormsSharedViewModel

class DopFormsDialogViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService,
    private val sharedViewModel: DopFormsSharedViewModel
) : ViewModel(), ICarDataListener {

    val errorLiveData = MutableLiveData<Failure>()

    private val _navigateBoolen = MutableLiveData<Boolean>()
    val navigateBoolean: LiveData<Boolean> get() = _navigateBoolen
    private val _clientBoolean = MutableLiveData<Boolean>()
    val clientBoolean: LiveData<Boolean> get() = _clientBoolean

    val carDataFromShareidViewModel: LiveData<AddCarRequest?> = sharedViewModel.getAddAvtoData()
    val clientDataFromSharedViewModel: LiveData<TestDataClass?> = sharedViewModel.getAddClientData()
    private val _phoneText = MutableLiveData<String>()
    val phoneText: LiveData<String> get() = _phoneText
    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_phoneText) { updateButtonState() } // Проверка при изменении EditText
        addSource(carDataFromShareidViewModel) { updateButtonState() } // Проверка при получении данных
    }
    val isButtonEnabled: LiveData<Boolean> get() = _isButtonEnabled


    override fun onCarDataReceived(addCarRequest: AddCarRequest) {
//        this.carData = addCarRequest
////        _carDataReceived.postValue(addCarRequest)
//        //  _carDataReceived.value = addCarRequest
//        _carDataReceived.postValue(addCarRequest)
//
//        Log.d("1234", "Data received in DopFormsViewModel: $carData")
//        Log.d("1234", "Data received in DopFormsViewModel: ${_carDataReceived.value}")
//        observeCarData()
    }
    fun setPhoneText(phone: String) {
        _phoneText.value = phone
    }

    private fun updateButtonState() {
        val isPhoneFilled = !_phoneText.value.isNullOrBlank()
        val isDataReceived = clientDataFromSharedViewModel.value != null

        Log.d("ButtonState", "isPhoneFilled: $isPhoneFilled, isDataReceived: $isDataReceived")

        _isButtonEnabled.value = isPhoneFilled && isDataReceived
    }



    fun clearShareData() {
        sharedViewModel.clearAvtoData()
        sharedViewModel.clearClientData()
    }

    fun validateAndInput(inputPhone: String) {
        if (clientDataFromSharedViewModel.value != null && inputPhone.isEmpty()) {
            errorLiveData.value = InputEditTextFailure()
            _clientBoolean.value = false
        } else if (clientDataFromSharedViewModel.value == null && inputPhone.isNotEmpty()) {
            errorLiveData.value = InputEditTextFailure()
            _clientBoolean.value = false
        } else if (clientDataFromSharedViewModel.value == null && inputPhone.isEmpty()) {
            errorLiveData.value = InputEditTextFailure()
            _clientBoolean.value = false
        }
        if (clientDataFromSharedViewModel.value != null && inputPhone.isNotEmpty()) {
            _clientBoolean.value = true
        }
    }


    fun callOnClickBtn() {

        if (carDataFromShareidViewModel.value != null) {
            _navigateBoolen.value = true
        } else {
            _navigateBoolen.value = false
            errorLiveData.value = InputEditTextFailure()

        }
    }


    companion object {
        fun create(context: Context): DopFormsDialogViewModel {
            val sharedViewModel =
                ViewModelProvider(context as FragmentActivity)[DopFormsSharedViewModel::class.java]
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            return DopFormsDialogViewModel(apiService, preferenceService, sharedViewModel)
        }
    }
}



