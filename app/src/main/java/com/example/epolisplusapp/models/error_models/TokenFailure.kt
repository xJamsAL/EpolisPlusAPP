package com.example.epolisplusapp.models.error_models

import android.content.Context
import com.example.epolisplusapp.R

class TokenFailure:Failure() {
    override fun getErrorMessage(context: Context): String {
        return context.getString(R.string.invalid_token)
    }
}