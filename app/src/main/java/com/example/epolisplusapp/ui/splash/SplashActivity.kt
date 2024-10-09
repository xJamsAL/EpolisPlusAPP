package com.example.epolisplusapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.epolisplusapp.R
import com.example.epolisplusapp.ui.auth.AuthLogin
import com.example.epolisplusapp.ui.auth.AuthMain
import com.example.epolisplusapp.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIMEOUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        enableEdgeToEdge()

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("access_token", null)
            Log.d("SplashActivity", "Token loaded: $accessToken")

            if (accessToken != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, AuthMain::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIMEOUT)
    }
}
