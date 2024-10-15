package com.example.epolisplusapp.models

data class BaseApiResponse<T>(
    val code: Boolean,
    val message: String,
    val response: T,
    val status: Int
)