package com.example.epolisplusapp.models.error_models

import android.content.Context
import com.example.epolisplusapp.R

class InputEditTextFailure:Failure() {
    override fun getErrorMessage(context: Context): String {
        return context.getString(R.string.input_fail)
    }
}