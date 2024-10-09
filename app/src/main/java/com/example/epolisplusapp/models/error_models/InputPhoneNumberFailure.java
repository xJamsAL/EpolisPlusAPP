package com.example.epolisplusapp.models.error_models;


import android.content.Context;

import com.example.epolisplusapp.R;

class InputPhoneNumberFailure extends Failure {
    @Override
    String getErrorMessage(Context context) {
        return context.getString(R.string.input_phone_number_failure);
    }
}
