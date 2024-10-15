package com.example.epolisplusapp.models.auth.response

data class LoginResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: TokenDataResponse
)
