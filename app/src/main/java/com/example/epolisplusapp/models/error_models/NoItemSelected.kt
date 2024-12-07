package com.example.epolisplusapp.models.error_models

import android.content.Context
import com.example.epolisplusapp.R

class NoItemSelected: Failure() {
    override fun getErrorMessage(context: Context): String {
        return context.getString(R.string.no_items_error)
    }

}