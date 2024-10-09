package com.example.epolisplusapp.models.auth

data class LoginResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: ResponseData
)
