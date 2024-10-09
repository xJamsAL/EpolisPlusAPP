package com.example.epolisplusapp.models.extra

data class ExtraResponse(
    val code: Boolean,
    val message: String,
    val response: Response,
    val status: Int
)