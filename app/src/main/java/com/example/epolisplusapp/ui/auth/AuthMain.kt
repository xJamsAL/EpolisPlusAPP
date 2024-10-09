package com.example.epolisplusapp.ui.auth

import com.example.epolisplusapp.retrofit.RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.epolisplusapp.databinding.AuthActivityMainBinding
import com.example.epolisplusapp.models.auth.CheckPhoneRequest
import com.example.epolisplusapp.models.auth.CheckPhoneResponse
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.PhoneNumberMaskWatcher
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.launch

class AuthMain : AppCompatActivity() {
    private lateinit var retrofitInstance: RetrofitInstance
    private lateinit var binding: AuthActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofitInstance = RetrofitInstance(this)
        binding = AuthActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edInputPhoneNumber.addTextChangedListener(
            PhoneNumberMaskWatcher(
                "(##) ###-##-##",
                binding.edInputPhoneNumber
            )
        )
        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, binding.progressBarBack, rootView)
        binding.btInput.setOnClickListener {
            val phonenumber = binding.edInputPhoneNumber.text.toString().trim()
            if (phonenumber.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните поле", Toast.LENGTH_SHORT).show()
            } else {
                checkPhoneNumber(phonenumber)
            }
        }

    }




    private fun checkPhoneNumber(phoneNumber: String) {
        lifecycleScope.launch {
            try {
                CommonUtils.hideKeyboard(this@AuthMain)
                binding.progressBarBack.visibility = View.VISIBLE
                val formattedPhoneNumber = CommonUtils.formatPhoneNumber(phoneNumber)
                val response: CheckPhoneResponse =
                    retrofitInstance.api.checkPhone(CheckPhoneRequest(formattedPhoneNumber))
                binding.progressBarBack.visibility = View.GONE

                if (response.response) {
                    val intent = Intent(this@AuthMain, AuthLogin::class.java).apply {
                        putExtra("number1", phoneNumber)
                    }
                    startActivity(intent)
                } else {
                    val intent = Intent(this@AuthMain, AuthRegister::class.java).apply {
                        putExtra("number1", phoneNumber)
                    }
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthMain, "Ошибка проверки номера", Toast.LENGTH_SHORT).show()
                binding.progressBarBack.visibility = View.GONE
            }
        }
    }
}

