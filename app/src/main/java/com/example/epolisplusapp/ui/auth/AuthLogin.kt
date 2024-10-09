package com.example.epolisplusapp.ui.auth

import com.example.epolisplusapp.retrofit.RetrofitInstance
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.epolisplusapp.databinding.AuthActivityLoginBinding
import com.example.epolisplusapp.models.auth.LoginRequest
import com.example.epolisplusapp.models.auth.LoginResponse
import com.example.epolisplusapp.ui.main.MainActivity
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.PhoneNumberMaskWatcher

import kotlinx.coroutines.launch

class AuthLogin : AppCompatActivity() {
    private lateinit var binding: AuthActivityLoginBinding
    private var isPasswordVisible: Boolean = false
    private lateinit var retrofitInstance: RetrofitInstance

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofitInstance = RetrofitInstance(this)

        val phoneNumber = intent.getStringExtra("number1")
        phoneNumber?.let {
            binding.edInputPhone.setText(it)
            binding.edInputPhone.isEnabled = false
        }

        binding.edInputPhone.addTextChangedListener(
            PhoneNumberMaskWatcher(" (##) ###-##-##", binding.edInputPhone)
        )

        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, binding.progressBarBack, rootView)

        binding.btLoginNext.setOnClickListener {
            val phoneNumber = binding.edInputPhone.text.toString().trim()
            val password = binding.edInputPass.text.toString().trim()
            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                login(phoneNumber, password)
            }
        }

        binding.edInputPass.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edInputPass.right - binding.edInputPass.compoundDrawables[2].bounds.width())) {
                    isPasswordVisible =
                        CommonUtils.setupPasswordVisibility(binding.edInputPass, isPasswordVisible)
                    v.performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.tvForgotPass.setOnClickListener {
            val intent = Intent(this, AuthForgotPass::class.java).apply {
                putExtra("number2", phoneNumber)
            }
            startActivity(intent)
        }


    }

    private fun login(phoneNumber: String, password: String) {
        lifecycleScope.launch {
            try {
                CommonUtils.hideKeyboard(this@AuthLogin)
                binding.progressBarBack.visibility = View.VISIBLE
                val formattedPhoneNumber = CommonUtils.formatPhoneNumber(phoneNumber)
                val response: LoginResponse =
                    retrofitInstance.api.login(LoginRequest(formattedPhoneNumber, password))

                binding.progressBarBack.visibility = View.GONE
                if (response.status == 200 && !response.code) {
                    Toast.makeText(this@AuthLogin, "Вход выполнен успешно!", Toast.LENGTH_SHORT)
                        .show()
                    val accessToken = response.response.access_token
                    val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("access_token", accessToken)
                    editor.apply()
                    val intent = Intent(this@AuthLogin, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {

                    Toast.makeText(
                        this@AuthLogin,
                        "Неверный номер телефона или пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                binding.progressBarBack.visibility = View.GONE
                Toast.makeText(
                    this@AuthLogin,
                    "Ошибка при входе.Неверный номер телефона или пароль",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}