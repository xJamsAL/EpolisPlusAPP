package com.example.epolisplusapp.ui.dopservice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest

class DopFormsSharedViewModel: ViewModel() {
    private val shareData = MutableLiveData<AddCarRequest?>()


    fun setData(data: AddCarRequest){
        Log.d("1234", "Updating data in SharedViewModel with: ${shareData.value}")
        shareData.value = data
        Log.d("1234", "Current sharedData value: ${data}")
    }

    fun getData(): LiveData<AddCarRequest?> = shareData
    fun clearData(){
        shareData.value = null
    }

}