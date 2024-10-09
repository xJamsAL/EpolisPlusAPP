package com.example.epolisplusapp.ui.auth

import com.example.epolisplusapp.retrofit.RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.epolisplusapp.databinding.AuthActivityForgotPassBinding
import com.example.epolisplusapp.models.auth.ForgotPasswordRequest
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.PhoneNumberMaskWatcher


import kotlinx.coroutines.launch


class AuthForgotPass : AppCompatActivity() {
    private lateinit var retrofitInstance:RetrofitInstance
    private lateinit var binder: AuthActivityForgotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = AuthActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binder.root)
        CommonUtils.setupToolbar(binder.toolbarForgot, this)
        setupListeners()
        retrofitInstance = RetrofitInstance(this)
        val phone = intent.getStringExtra("number2")
        phone?.let {
            binder.edInputForgotPhone.setText(it)
        }
        binder.edInputForgotPhone.addTextChangedListener(
            PhoneNumberMaskWatcher(" (##) ###-##-##", binder.edInputForgotPhone)
        )
        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, binder.progressBarBack, rootView)


    }

    private fun setupListeners() {
        binder.btSendSms.setOnClickListener {
            val phoneNumber = binder.edInputForgotPhone.text.toString().trim()
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите номер телефона", Toast.LENGTH_SHORT)
                    .show()
            } else {
                sendForgotPasswordRequest(phoneNumber)
            }
        }
    }

    private fun sendForgotPasswordRequest(phoneNumber: String) {
        lifecycleScope.launch {
            try {
                CommonUtils.hideKeyboard(this@AuthForgotPass)
                binder.progressBarBack.visibility = View.VISIBLE
                val formattedPhoneNumber  = CommonUtils.formatPhoneNumber(phoneNumber)
                val response =
                    retrofitInstance.api.forgotPassword(ForgotPasswordRequest(formattedPhoneNumber))
                binder.progressBarBack.visibility = View.GONE
                if (response.status == 200) {
                    val intent = Intent(this@AuthForgotPass, AuthForgotCodePass::class.java)
                    intent.putExtra("phone_number", formattedPhoneNumber)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@AuthForgotPass,
                        "Ошибка: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthForgotPass, "Ошибка: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                binder.progressBarBack.visibility = View.GONE
            }
        }
    }

}