package com.example.epolisplusapp.ui.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.epolisplusapp.R
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar

class SettingNotifycationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.setting_notifycation_activity)
        val toolbar:MaterialToolbar = findViewById(R.id.toolbarSetNotify)
        CommonUtils.setupToolbar(toolbar, this)

    }
}