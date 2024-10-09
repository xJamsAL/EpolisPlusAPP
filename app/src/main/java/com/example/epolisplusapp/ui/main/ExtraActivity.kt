package com.example.epolisplusapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.ExtraAdapter
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.databinding.ExtraServiceActivityBinding
import com.example.epolisplusapp.models.extra.Risk
import com.example.epolisplusapp.retrofit.RetrofitInstance
import com.example.epolisplusapp.util.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ExtraActivity : AppCompatActivity() {

    private lateinit var extraAdapter: ExtraAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: MainApi.ApiService
    private lateinit var binding: ExtraServiceActivityBinding
    private var riskItem: List<Risk> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExtraServiceActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, binding.extraBar, rootView)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        CommonUtils.setupToolbar(binding.extraToolbar, this)
        binding.rcExtraService.layoutManager = LinearLayoutManager(this)
        extraAdapter = ExtraAdapter(riskItem, R.string.dop_uslugi_currency, this)
        binding.rcExtraService.adapter = extraAdapter
        apiService = RetrofitInstance(this).api



        emergensyList()
        enableEdgeToEdge()
    }


    private fun emergensyList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    binding.extraBar.visibility = View.VISIBLE
                }
                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken != null) {
                    val response = apiService.getEmergency("Bearer $accessToken")
                    withContext(Dispatchers.Main) {
                        val riskList: List<Risk> = response.response.risk
                        val phone = response.response.phone
                        savePhoneToPreferences(phone)
                        val updatedCallCenter =
                            sharedPreferences.getString("call_center_phone", null)

                        if (!updatedCallCenter.isNullOrEmpty()) {
                            val formattedPhone = "tel:$updatedCallCenter"
                            binding.btExtraCall.setOnClickListener {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(formattedPhone))
                                startActivity(intent)
                            }
                        }
                        if (riskList.isNotEmpty()) {
                            extraAdapter.updateData(riskList)

                        } else {
                            Toast.makeText(
                                this@ExtraActivity,
                                "Список услуг пуст",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("ExtraActivity", "Список услуг пуст")
                        }
                        binding.extraBar.visibility = View.GONE

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ExtraActivity,
                            "Токен доступа не найден",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.extraBar.visibility = View.GONE
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ExtraActivity,
                        "Ошибка при загрузке данных: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.extraBar.visibility = View.GONE
                }
            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ExtraActivity,
                        "Неизвестная ошибка: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.extraBar.visibility = View.GONE
                }
            }
        }
    }

    private fun savePhoneToPreferences(phone: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("call_center_phone", phone)
        editor.apply()

    }


}