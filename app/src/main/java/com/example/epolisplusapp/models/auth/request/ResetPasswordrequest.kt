package com.example.epolisplusapp.models.auth.request

data class ResetPasswordRequest(
    val phone: String,
    val code: String,
    val password: String,
    val password_repeat: String
)

data class ResetPasswordResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: Any?
)