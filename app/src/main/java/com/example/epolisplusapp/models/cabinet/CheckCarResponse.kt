package com.example.epolisplusapp.models.cabinet

data class CheckCarResponse(
    val code: Boolean,
    val message: String,
    val response: Response,
    val status: Int
)