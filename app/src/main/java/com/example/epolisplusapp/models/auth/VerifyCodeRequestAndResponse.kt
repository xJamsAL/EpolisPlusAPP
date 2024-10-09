package com.example.epolisplusapp.models.auth

data class VerifyCodeRequest(
    val phone: String,
    val code: String
)

data class VerifyCodeResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: ResponseData
)

