package com.example.epolisplusapp.models.error_models

import android.content.Context
import com.example.epolisplusapp.R

class GenericFailure:  Failure() {
    override fun getErrorMessage(context: Context): String {
        return context.getString(R.string.unknow_error)
    }
}