package com.example.epolisplusapp.ui.auth

import com.example.epolisplusapp.retrofit.RetrofitInstance
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.epolisplusapp.databinding.AuthActivityRegistrationBinding
import com.example.epolisplusapp.models.auth.RegisterRequest
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.PhoneNumberMaskWatcher

import kotlinx.coroutines.launch

class AuthRegister : AppCompatActivity() {
    private lateinit var binding: AuthActivityRegistrationBinding
    private var isPasswordVisible: Boolean = false
    private lateinit var retrofitInstance: RetrofitInstance

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofitInstance = RetrofitInstance(this)
        CommonUtils.setupToolbar(binding.toolbarReg,this)
        val phone = intent.getStringExtra("number1")
        phone?.let {
            binding.edInputPhoneReg.setText(it)
        }

        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, binding.progressBarBack, rootView)

        binding.edInputPhoneReg.addTextChangedListener(
            PhoneNumberMaskWatcher(" (##) ###-##-##", binding.edInputPhoneReg)
        )

        binding.btRegis.setOnClickListener {
            val name = binding.edInputNameReg.text.toString().trim()
            val phone = binding.edInputPhoneReg.text.toString().trim()
            val pass = binding.edInputPassReg.text.toString().trim()
            val confirmPass = binding.edInputConfPassReg.text.toString().trim()
            val checkCheckBox = binding.checkBoxPersonal.isChecked

            if (name.isEmpty() || phone.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else if (pass.length < 8) {
                Toast.makeText(
                    this,
                    "Пароль должен содержать минимум 8 символов",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!checkCheckBox) {
                Toast.makeText(
                    this,
                    "Пожалуйста, примите условия использования",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass != confirmPass) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            } else {
                val formattedPhoneNumber = CommonUtils.formatPhoneNumber(phone)
                performRegis(name, formattedPhoneNumber, pass, confirmPass)
            }
        }

        binding.edInputPassReg.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edInputPassReg.right - binding.edInputPassReg.compoundDrawables[2].bounds.width())) {
                    isPasswordVisible =
                        CommonUtils.setupPasswordVisibility(binding.edInputPassReg, isPasswordVisible)
                    v.performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }
        binding.edInputConfPassReg.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edInputConfPassReg.right - binding.edInputConfPassReg.compoundDrawables[2].bounds.width())) {
                    isPasswordVisible =
                        CommonUtils.setupPasswordVisibility(binding.edInputConfPassReg, isPasswordVisible)
                    v.performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun performRegis(name: String, phone: String, pass: String, confirmPass: String) {
        val request = RegisterRequest(
            first_name = name,
            last_name = "",
            phone = phone,
            email = "",
            password = pass,
            password_repeat = confirmPass
        )

        lifecycleScope.launch {
            try {
                CommonUtils.hideKeyboard(this@AuthRegister)
                binding.progressBarBack.visibility = View.VISIBLE
                val response = retrofitInstance.api.signUp(request)
                binding.progressBarBack.visibility = View.GONE
                if (response.status == 200 && response.message.contains("Sms verification send")) {
                    val intent = Intent(this@AuthRegister, AuthVerification::class.java).apply {
                        putExtra("PHONE_NUMBER", phone)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@AuthRegister,
                        "Ошибка регистрации: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthRegister, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progressBarBack.visibility = View.GONE
            }

        }


    }


}