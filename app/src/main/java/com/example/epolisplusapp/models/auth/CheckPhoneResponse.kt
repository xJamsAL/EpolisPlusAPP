package com.example.epolisplusapp.models.auth


data class CheckPhoneResponse(
    val status: Int,
    val code: Boolean,
    val message: String,
    val response: Boolean
)
