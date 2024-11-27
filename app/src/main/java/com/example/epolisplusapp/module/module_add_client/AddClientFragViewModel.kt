package com.example.epolisplusapp.module.module_add_client

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.models.TestDataClass
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.InputEditTextFailure
import com.example.epolisplusapp.ui.dopservice.DopFormsSharedViewModel

class AddClientFragViewModel(
    private val sharedViewModel: DopFormsSharedViewModel
) : ViewModel() {

    companion object {
        fun create(context: Context): AddClientFragViewModel {
            val sharedViewModel =
                ViewModelProvider(context as FragmentActivity)[DopFormsSharedViewModel::class.java]
            return AddClientFragViewModel(sharedViewModel)
        }
    }


    val erroLiveData = MutableLiveData<Failure>()
    private val _navigateAddClientNext = MutableLiveData<Boolean>()


    private val _output1 = MutableLiveData<TestDataClass>()
    val output1: LiveData<TestDataClass> = _output1

    fun testSendData(input1: String, input2: String, input3: String) {
        if (input1.isBlank() || input2.isBlank() || input3.isBlank()) {
            erroLiveData.value = InputEditTextFailure()
            _navigateAddClientNext.value = false
        } else {
            val responseData = TestDataClass(
                id = 1,
                fullName = input1,
                pinfl = "TEST TEST TEST TES:ONLINE TES:SKYRIM"
            )
            _output1.value = responseData
            _navigateAddClientNext.value = true
            sharedViewModel.setAddClientData(responseData)
        }


    }

    fun clearClientData() {
        Log.d("1234", "clear data ${_output1.value}")
        if (_output1.value != null) {
            sharedViewModel.clearClientData()
            Log.d("1234", "Данные очищены")
            Log.d("1234", "Данные ${_output1.value}")
        } else {
            erroLiveData.value = GenericFailure()
        }

    }
}