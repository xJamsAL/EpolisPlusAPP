package com.example.epolisplusapp.models.error_models

import android.content.Context
import com.example.epolisplusapp.R

class NetworkFailure(private val message:String) : Failure() {
    override fun getErrorMessage(context: Context): String {
        return message
    }
}