package com.example.epolisplusapp.models.error_models;

import android.content.Context;

public abstract class Failure {
    abstract String getErrorMessage(Context requireContext);

}
