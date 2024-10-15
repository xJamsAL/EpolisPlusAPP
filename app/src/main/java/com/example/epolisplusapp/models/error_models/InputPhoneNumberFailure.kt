package com.example.epolisplusapp.models.error_models

import android.content.Context
import com.example.epolisplusapp.R

class InputPhoneNumberFailure: Failure() {
    override fun getErrorMessage(context: Context): String {
        return context.getString(R.string.phone_fail)
    }
}