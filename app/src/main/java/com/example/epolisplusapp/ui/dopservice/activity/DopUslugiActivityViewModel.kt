package com.example.epolisplusapp.ui.dopservice.activity

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.dopuslugi.LitroCalculatorItems
import com.example.epolisplusapp.models.error_models.Failure
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.NetworkFailure
import com.example.epolisplusapp.models.error_models.NoItemSelected
import com.example.epolisplusapp.models.error_models.TokenFailure
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.ui.dopservice.DopFormsSharedViewModel
import com.example.epolisplusapp.util.CommonUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DopUslugiActivityViewModel(
    private val apiService: MainApi.ApiService,
    private val preferenceService: PreferenceService,
    private val sharedviewmodel: DopFormsSharedViewModel

) : ViewModel() {

    val errorMessageLiveData = MutableLiveData<Failure>()
    val progressBarVisible = MutableLiveData<Boolean>()
    val dopListLiveData = MutableLiveData<List<LitroCalculatorItems>>()
    val discountDataLiveData = MutableLiveData<Pair<Int, Int>>()
    val discountTitleLiveData = MutableLiveData<String>()
    val discountBodyLiveData = MutableLiveData<String>()
    private val _itemSelectedLiveData = MutableLiveData<Set<Int>>(emptySet())
    private val _navigateBoolean = MutableLiveData<Boolean>()
    val navigateBoolean: LiveData<Boolean> get() = _navigateBoolean
    val itemSelectedLiveData: LiveData<Set<Int>> get() = _itemSelectedLiveData

    private val _selectedItemsLiveData = MutableLiveData<List<LitroCalculatorItems>>()
    val selectedItemsLiveData: LiveData<List<LitroCalculatorItems>> get() = _selectedItemsLiveData
    private val _totalSum = MutableLiveData(0)
    val totalSum: LiveData<Int> = _totalSum
    val discount = MutableLiveData(0)
    val finalTotal = MutableLiveData(0)
    private var discountPercent = 0
    private var discountLength = 0

    fun checkItems() {
        if (_itemSelectedLiveData.value?.isNotEmpty() == true) {
            _navigateBoolean.value = true
        } else {
            _navigateBoolean.value = false
            errorMessageLiveData.value = NoItemSelected()
        }
    }

    fun toggleItemSelection(position: Int, itemPrice: Int) {
        val selectedItems = itemSelectedLiveData.value?.toMutableSet() ?: mutableSetOf()

        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
            _totalSum.value = (_totalSum.value ?: 0) - itemPrice

        } else {
            selectedItems.add(position)
            _totalSum.value = (_totalSum.value ?: 0) + itemPrice
        }

        _itemSelectedLiveData.value = selectedItems
        Log.d("adapter", "itemSelectedLiveData  ${itemSelectedLiveData.value}")
        calculateDiscountAndFinalTotal()
    }

    private fun calculateDiscountAndFinalTotal() {
        val total = _totalSum.value ?: 0
        val itemCount = _itemSelectedLiveData.value?.size ?: 0
        val calculatedDiscount =
            if (itemCount >= discountLength) (total * discountPercent / 100) else 0
        discount.value = calculatedDiscount
        finalTotal.value = total - calculatedDiscount

        sharedviewmodel.setDiscount(calculatedDiscount)
        sharedviewmodel.setFinalTotal(total - calculatedDiscount)
        Log.d("1234", "discount = ${discount.value} finalTotal = ${finalTotal.value}")
    }

    fun fetcLitroList() {
        viewModelScope.launch {
            progressBarVisible.value = true
            try {
                val accessToken = preferenceService.getAccessToken()
                if (accessToken.isNotEmpty()) {
                    val response = apiService.litroCalculator("Bearer $accessToken")
                    dopListLiveData.value = response.response.risk
                    discountDataLiveData.value = Pair(
                        response.response.discount_percent,
                        response.response.discount_length,
                    )
                    sharedviewmodel.setDiscountData(
                        response.response.discount_percent.toString()
                    )
                    discountLength = response.response.discount_length
                    discountPercent = response.response.discount_percent
//                    Log.d(
//                        "adapter",
//                        "discountLength and discountPrecent = $discountPercent $discountLength"
//                    )
//                    Log.d("adapter", "dopListLiveData ${dopListLiveData.value}")
//                    Log.d(
//                        "adapter",
//                        "discount in viewmodel = ${response.response.discount_percent}and${response.response.discount_length}"
//                    )
                    discountTitleLiveData.value = response.response.discount_title
                    discountBodyLiveData.value = response.response.discount_body
                    Log.d("1234", "${discountDataLiveData.value}")
                } else {
                    errorMessageLiveData.value = TokenFailure()
                }
            } catch (e: HttpException) {
                errorMessageLiveData.value = NetworkFailure(e.message())
            } catch (e: Exception) {
                errorMessageLiveData.value = GenericFailure()
            } finally {
                progressBarVisible.value = false
            }

        }
    }

    companion object {
        fun create(context: Context): DopUslugiActivityViewModel {
            val preferenceService = PreferenceService.getInstance(context)
            val apiService = RetrofitInstance(context).api
            val sharedViewModel = ViewModelProvider(context as FragmentActivity)[DopFormsSharedViewModel::class.java]
            return DopUslugiActivityViewModel(apiService, preferenceService, sharedViewModel)
        }
    }
}