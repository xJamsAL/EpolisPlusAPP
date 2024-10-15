package com.example.epolisplusapp.models.auth.response


data class CheckPhoneResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: Boolean
)
