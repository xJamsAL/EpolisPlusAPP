package com.example.epolisplusapp.models.error_models

import android.content.Context

class ApiErrorMessage(private val apiErrorMessage: String): Failure() {
    override fun getErrorMessage(context: Context): String {
        return apiErrorMessage
    }
}