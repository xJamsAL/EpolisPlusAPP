package com.example.epolisplusapp.models.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private val _textData = MutableLiveData<String>()
    val textData: LiveData<String> get() = _textData
    private val _phoneData = MutableLiveData<String>()
    val phoneData: LiveData<String> get() = _phoneData
    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> get() = _showProgressBar

    fun setTextData(text: String) {
        _textData.value = text
    }
    fun setPhoneData(phone: String) {
        _phoneData.value = phone
    }
    fun setShowProgressBar(show: Boolean) {
        _showProgressBar.value = show
    }
}