package com.example.epolisplusapp.ui.dopservice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epolisplusapp.models.TestDataClass
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest

class DopFormsSharedViewModel : ViewModel() {
    private val addAvtoData = MutableLiveData<AddCarRequest?>()
    private val addClientData = MutableLiveData<TestDataClass?>()


    fun setAddAvtoData(data: AddCarRequest) {
        addAvtoData.value = data
    }

    fun setAddClientData(data : TestDataClass) {
        addClientData.value = data
    }

    fun getAddAvtoData(): LiveData<AddCarRequest?> = addAvtoData
    fun getAddClientData(): LiveData<TestDataClass?> = addClientData

    fun clearAvtoData() {
        addAvtoData.value = null
    }

    fun clearClientData() {
        addClientData.value = null
    }

}