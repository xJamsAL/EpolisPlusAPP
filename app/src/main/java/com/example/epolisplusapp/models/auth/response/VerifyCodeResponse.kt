package com.example.epolisplusapp.models.auth.response


data class VerifyCodeResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: TokenDataResponse
)

