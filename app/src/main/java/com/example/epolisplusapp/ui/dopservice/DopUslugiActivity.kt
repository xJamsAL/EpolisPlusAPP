package com.example.epolisplusapp.ui.dopservice

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.DopUslugiAdapter
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.databinding.DopUslugiActivityBinding
import com.example.epolisplusapp.models.dopuslugi.DopItems
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.util.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Locale

class DopUslugiActivity : AppCompatActivity() {

    private lateinit var dopuslugiAdapter: DopUslugiAdapter
    private lateinit var apiService: MainApi.ApiService
    private lateinit var binding: DopUslugiActivityBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var items: Int? = null

    private var discountPercent: Int = 0
    private var discountLength: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DopUslugiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = findViewById<View>(android.R.id.content)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        binding.rcDop.layoutManager = GridLayoutManager(this, 2)
        CommonUtils.setupToolbar(binding.dopToolbar, this)
        CommonUtils.setupBlurView(this, binding.dopBar, rootView)


        dopuslugiAdapter = DopUslugiAdapter(
            emptyList(), R.string.dop_uslugi_currency, this, discountPercent, discountLength,
            { itemCount, totalSum ->
                items = itemCount
                val discount =
                    if (itemCount >= discountLength) (totalSum * discountPercent / 100).toInt() else 0
                val finalTotal = totalSum - discount

                binding.tvTotalSum.text = CommonUtils.formatSumWithSeparatorAndCurrency(
                    totalSum,
                    Locale("ru", "Ru"),
                    this,
                    R.string.dop_uslugi_currency
                )
                binding.tvDiscount.text = CommonUtils.formatSumWithSeparatorAndCurrency(
                    discount,
                    Locale("ru", "Ru"),
                    this,
                    R.string.dop_uslugi_currency
                )
                binding.tvFinalSum.text = CommonUtils.formatSumWithSeparatorAndCurrency(
                    finalTotal,
                    Locale("ru", "Ru"),
                    this,
                    R.string.dop_uslugi_currency
                )
            },
            { selectedItem ->
                val bottomSheetFragment = DopUslugiDialogFragment.newInstance(
                    selectedItem.name,
                    selectedItem.icon,
                    selectedItem.price,
                    selectedItem.info
                )
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        )
        binding.rcDop.adapter = dopuslugiAdapter

        apiService = RetrofitInstance(this).api
        fetchDopUslugiData()
        binding.tvDopDiscount.text = discountPercent.toString()

        binding.btOformit.setOnClickListener {
            if (items != null) {
                Log.d("1234", "$items")
                val bottomSheet = DopFormsFragment()
                bottomSheet.show(supportFragmentManager, "myBottomSheet")
            } else{
                Toast.makeText(this, "Выберите элемент перед продолжением", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun fetchDopUslugiData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    binding.dopBar.visibility = View.VISIBLE
                }
                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken != null) {
                    val response = apiService.getDopUslugi("Bearer $accessToken")
                    withContext(Dispatchers.Main) {
                        val dopList: List<DopItems> = response.response.risk
                        discountPercent = response.response.discount_percent
                        discountLength = response.response.discount_length
                        dopuslugiAdapter.updateData(dopList, discountPercent, discountLength)
                        binding.tvDopPresent.text = response.response.discount_title
                        binding.tvDopPresent2.text = response.response.discount_body
                        binding.tvDopDiscount.text = discountPercent.toString()


                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@DopUslugiActivity,
                            "Токен доступа не найден",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DopUslugiActivity,
                        "Ошибка при загрузке данных: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DopUslugiActivity,
                        "Неизвестная ошибка: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.dopBar.visibility = View.GONE
                }
            }
        }
    }


//     fun onClick() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                Log.d("1234", "onClick called")
//                val accessToken = sharedPreferences.getString("access_token", null)
//                if (accessToken == null) {
//                    showToast("Токен недействителен")
//                    return@launch
//                }
//                val checkCarRequest = dopviewModel.getCheckCarRequest()
//                if (checkCarRequest == null) {
//                    showToast("Нет сохраненных данных для отправки")
//                    return@launch
//                }
//
//                val addCarRequest = dopviewModel.getAddCarRequest()
//                if (addCarRequest == null) {
//                    showToast("Нет данных для отправки")
//                    return@launch
//                }
//
//                Log.d("1234", "Сохраненные данные для отправки: $addCarRequest")
//                val addCarResponse = apiService.addCar("Bearer $accessToken", addCarRequest)
//
//                withContext(Dispatchers.Main) {
//                    if (addCarResponse.status == 200) {
//                        showToast("Данные успешно отправлены")
//                        finish()
//                    } else {
//                        showToast("Ошибка: ${addCarResponse.message}")
//                    }
//                }
//            } catch (e: HttpException) {
//                showToast("Ошибка сети: ${e.message}")
//                Log.e("1234", "Ошибка сети: ${e.message}")
//            } catch (e: Exception) {
//                showToast("Произошла ошибка: ${e.message}")
//                Log.e("1234", "Ошибка: ${e.message}")
//            }
//        }
//    }


    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@DopUslugiActivity, message, Toast.LENGTH_SHORT).show()
        }
    }


}
