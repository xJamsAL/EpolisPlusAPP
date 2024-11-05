package com.example.epolisplusapp.models.auth.request

data class VerifyCodeRequest(
    val phone: String,
    val code: String
)
