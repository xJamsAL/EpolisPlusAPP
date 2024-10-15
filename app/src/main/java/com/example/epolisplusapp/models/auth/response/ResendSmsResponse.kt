package com.example.epolisplusapp.models.auth.response

data class ResendSmsResponse (
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: Any?
)