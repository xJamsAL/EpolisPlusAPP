package com.example.epolisplusapp.ui.auth

import com.example.epolisplusapp.service.RetrofitInstance
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.auth.ResetPasswordRequest
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.launch

class AuthForgotCodePass : AppCompatActivity() {
    private var phoneNumber: String? = null
    private var isPasswordVisible = false
    private lateinit var retrofitInstance: RetrofitInstance


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity_forgot_codepass)
        val toolbar: MaterialToolbar = findViewById(R.id.tollbarCodePass)
        val rootView = findViewById<View>(android.R.id.content)
        val progressBarBack = findViewById<BlurView>(R.id.progressBarBack)
        CommonUtils.setupBlurView(this, progressBarBack, rootView)
        CommonUtils.setupToolbar(toolbar, this)
        CommonUtils.setupEditTexts(this,
            containerId = R.id.tvCodePass,
            editTextIds = intArrayOf(
                R.id.etCode1,
                R.id.etCode2,
                R.id.etCode3,
                R.id.etCode4,
                R.id.etCode5
            )
        )

        phoneNumber = intent.getStringExtra("phone_number")


        val edPassword: EditText = findViewById(R.id.edCodePass)
        val edPasswordRepeat: EditText = findViewById(R.id.edRepeatCodePass)
        val btConfirm: Button = findViewById(R.id.btConfAndNext)

        retrofitInstance = RetrofitInstance(this)

        btConfirm.setOnClickListener {
            val code = getCodeFromInputs()
            val password = edPassword.text.toString()
            val passwordRepeat = edPasswordRepeat.text.toString()

            if (password.isEmpty() || code.isEmpty() || passwordRepeat.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }


            if (password != passwordRepeat) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }
            if (!areAllEditTextsFilled(intArrayOf(
                    R.id.etCode1,
                    R.id.etCode2,
                    R.id.etCode3,
                    R.id.etCode4,
                    R.id.etCode5
                ))) {
                Toast.makeText(this, "Введите код полностью", Toast.LENGTH_SHORT).show()
            }
            val request = ResetPasswordRequest(phoneNumber!!, code, password, passwordRepeat)
            resetPassword(request)
        }


        edPassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (edPassword.right - edPassword.compoundDrawables[2].bounds.width())) {
                    isPasswordVisible = CommonUtils.setupPasswordVisibility(edPassword, isPasswordVisible)
                    v.performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }
        edPasswordRepeat.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (edPasswordRepeat.right - edPasswordRepeat.compoundDrawables[2].bounds.width())) {
                    isPasswordVisible = CommonUtils.setupPasswordVisibility(edPasswordRepeat, isPasswordVisible)
                    v.performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }


    }

    private fun areAllEditTextsFilled(editTextIds: IntArray): Boolean {
        return editTextIds.all { id ->
            val editText = findViewById<EditText>(id)
            editText.text.isNotEmpty()
        }
    }

    private fun getCodeFromInputs(): String {
        val edCode1: EditText = findViewById(R.id.etCode1)
        val edCode2: EditText = findViewById(R.id.etCode2)
        val edCode3: EditText = findViewById(R.id.etCode3)
        val edCode4: EditText = findViewById(R.id.etCode4)
        val edCode5: EditText = findViewById(R.id.etCode5)
        return edCode1.text.toString() +
                edCode2.text.toString() +
                edCode3.text.toString() +
                edCode4.text.toString() +
                edCode5.text.toString()
    }


    private fun resetPassword(request: ResetPasswordRequest) {
        val progBack: LinearLayout = findViewById(R.id.progressBarBack)
        lifecycleScope.launch {
            try {
                CommonUtils.hideKeyboard(this@AuthForgotCodePass)
                progBack.visibility = View.VISIBLE
                val response = retrofitInstance.api.resetPassword(request)
                progBack.visibility = View.GONE
                if (response.status == 200) {
                    Toast.makeText(this@AuthForgotCodePass, response.message, Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@AuthForgotCodePass, AuthLogin::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this@AuthForgotCodePass,
                        "Ошибка: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AuthForgotCodePass, "Ошибка: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                progBack.visibility = View.GONE
            }
        }
    }
}
