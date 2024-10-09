package com.example.epolisplusapp.ui.auth

import com.example.epolisplusapp.service.RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.AuthActivityVerificationBinding
import com.example.epolisplusapp.models.auth.ResendSmsRequest
import com.example.epolisplusapp.models.auth.VerifyCodeRequest
import com.example.epolisplusapp.ui.main.MainActivity
import com.example.epolisplusapp.util.CommonUtils
import kotlinx.coroutines.launch

class AuthVerification : AppCompatActivity() {
    lateinit var binding: AuthActivityVerificationBinding
    private var timer: CountDownTimer? = null
    private lateinit var retrofitInstance: RetrofitInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofitInstance = RetrofitInstance(this)
        CommonUtils.setupToolbar(binding.toolbarVerf, this)
        startTimer()
        binding.tvResetCode.setOnClickListener {
            resendSms()
        }
        binding.btConfirmAndNext.setOnClickListener {
            verifyCode()
        }
        val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
        binding.tvPhoneNumber.text = phoneNumber

        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupBlurView(this, binding.progressBarBack, rootView)

        CommonUtils.setupEditTexts(this,
            containerId = R.id.tvVerfContainer,
            editTextIds = intArrayOf(
                R.id.edCode1,
                R.id.edCode2,
                R.id.edCode3,
                R.id.edCode4,
                R.id.edCode5
            )

        )

    }

    private fun startTimer() {
        binding.tvTimer.visibility = View.VISIBLE
        binding.tvResetCode.visibility = View.GONE

        timer = object : CountDownTimer(61000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }


            override fun onFinish() {
                binding.tvTimer.visibility = View.GONE
                binding.tvResetCode.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun resendSms() {
        val phoneNumber = binding.tvPhoneNumber.text.toString()
        val formattedPhoneNumber = CommonUtils.formatPhoneNumber(phoneNumber)
        val request = ResendSmsRequest(phone = formattedPhoneNumber)
        lifecycleScope.launch {
            try {
                val response = retrofitInstance.api.resendSms(request)
                if (response.status == 200 && !response.code) {
                    Toast.makeText(
                        this@AuthVerification,
                        "SMS отправлено снова",
                        Toast.LENGTH_SHORT
                    ).show()
                    startTimer()
                } else {
                    Toast.makeText(this@AuthVerification, "Ошибка отправки SMS", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthVerification, "Ошибка: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun verifyCode() {
        val code = getCodeFromInputs()
        val phoneNumber = binding.tvPhoneNumber.text.toString()

        if (code.length == 5) {
            lifecycleScope.launch {
                CommonUtils.hideKeyboard(this@AuthVerification)

                try {
                    CommonUtils.hideKeyboard(this@AuthVerification)
                    binding.progressBarBack.visibility = View.VISIBLE
                    val request = VerifyCodeRequest(phone = phoneNumber, code = code)
                    val response = retrofitInstance.api.verifyCode(request)
                    binding.progressBarBack.visibility = View.GONE
                    if (response.status == 200) {
                        Toast.makeText(
                            this@AuthVerification,
                            "Код успешно верифицирован",
                            Toast.LENGTH_SHORT
                        ).show()
                        val accessToken = response.response.access_token
                        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("access_token", accessToken)
                        editor.apply()

                        val intent = Intent(this@AuthVerification, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@AuthVerification,
                            "Ошибка верификации кода: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@AuthVerification, AuthMain::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    binding.progressBarBack.visibility = View.GONE
                    Toast.makeText(
                        this@AuthVerification,
                        "Ошибка: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "Пожалуйста, введите полный код", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCodeFromInputs(): String {
        return binding.edCode1.text.toString() +
                binding.edCode2.text.toString() +
                binding.edCode3.text.toString() +
                binding.edCode4.text.toString() +
                binding.edCode5.text.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}


