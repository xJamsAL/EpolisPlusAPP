package com.example.epolisplusapp.ui.dopservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest

class SharedDopServiceViewModel:ViewModel() {
    private val _addCarRequest = MutableLiveData<AddCarRequest?>()
    val addCarRequest: LiveData<AddCarRequest?> get() = _addCarRequest
    fun setAddCarRequest (request: AddCarRequest){
        _addCarRequest.value = request
    }
}