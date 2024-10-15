package com.example.epolisplusapp.models.auth.request

data class RegisterRequest(
    val first_name: String,
    val last_name: String,
    val phone: String,
    val email: String,
    val password: String,
    val password_repeat: String
)

data class RegisterResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: Any?
)

