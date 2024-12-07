package com.example.epolisplusapp.ui.dopservice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epolisplusapp.models.TestDataClass
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.dopuslugi.LitroCalculatorItems

class DopFormsSharedViewModel : ViewModel() {
    private val addAvtoData = MutableLiveData<AddCarRequest?>()
    private val addClientData = MutableLiveData<TestDataClass?>()
    private val selectedItems = MutableLiveData<List<LitroCalculatorItems>>()
    private val discountData = MutableLiveData<String>()
    private val discountPrice = MutableLiveData(0)
    private val finalTotal = MutableLiveData(0)


    fun setDiscount(value: Int){
        Log.d("1234", "Updating discount value $value")
        discountPrice.value = value
        Log.d("1234", "Updating discount value in SharedViewModel  ${discountPrice.value}")

    }
    fun setFinalTotal(value: Int){
        Log.d("1234", "Updating finalTotal value $value")
        finalTotal.value = value
        Log.d("1234", "Updating finalTotal value in SharedViewModel ${finalTotal.value}")

    }

    fun updateSelectedItems(items: List<LitroCalculatorItems>) {
//        Log.d("adapter", "Updating selected items: $items")
        selectedItems.value = items
//        Log.d("adapter", "Updated selected items in SharedViewModel: ${selectedItems.value}")
    }
    fun setDiscountData(percent: String){
        Log.d("1234", "Updating discountPercent $percent")
        discountData.value = percent
        Log.d("1234", "Updating discountData.value ${discountData.value}")


    }

    fun setAddAvtoData(data: AddCarRequest) {
        addAvtoData.value = data
    }

    fun setAddClientData(data: TestDataClass) {

        addClientData.value = data
    }

    fun getDiscountPrice():LiveData<Int> = discountPrice
    fun getFinalTotal(): LiveData<Int> = finalTotal
    fun getDiscountData(): LiveData<String> = discountData
    fun getRecycleItems(): LiveData<List<LitroCalculatorItems>> = selectedItems
    fun getAddAvtoData(): LiveData<AddCarRequest?> = addAvtoData
    fun getAddClientData(): LiveData<TestDataClass?> = addClientData

    fun clearAvtoData() {
        addAvtoData.value = null
    }

    fun clearClientData() {
        addClientData.value = null
    }

}