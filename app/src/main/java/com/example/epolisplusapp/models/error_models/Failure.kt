package com.example.epolisplusapp.models.error_models

import android.content.Context

abstract class Failure {
    abstract fun getErrorMessage(context: Context): String
}