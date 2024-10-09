package com.example.epolisplusapp.models.error_models;

import android.content.Context;

public class InputVehicleInformationFailure extends Failure {
    @Override
    public String getErrorMessage(Context requireContext) {
        return "san avtomashinani danniylarini kiritishing kerak";
    }
}
