package com.example.epolisplusapp.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.SettingsActivityBinding
import com.example.epolisplusapp.ui.auth.AuthMain
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtils.setupToolbar(binding.toolbarSetting, this)
        sharedPreferences = this.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        binding.btSetNotification.setOnClickListener {
            val intent = Intent(this, SettingNotifycationActivity::class.java)
            startActivity(intent)
        }

        binding.btSetLanguage.setOnClickListener {
            val languageDialog = LanguageDialog()
            languageDialog.show(supportFragmentManager, languageDialog.tag)
        }
        binding.btSetAbout.setOnClickListener {
            Toast.makeText(this, "About button clicked", Toast.LENGTH_SHORT).show()
        }
        binding.btSetSupport.setOnClickListener {
            Toast.makeText(this, "Support button clicked", Toast.LENGTH_SHORT).show()
        }
        binding.btSetQuestions.setOnClickListener {
            Toast.makeText(this, "Queations button clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {

        with(sharedPreferences.edit()) {
            clear()
            apply()
            val intent = Intent(
                this@SettingsActivity,
                com.example.epolisplusapp.ui.auth.AuthMain::class.java
            )
            startActivity(intent)
            finish()
        }
    }
}