package com.example.epolisplusapp.models.cabinet

data class AddCarResponse(
    val code: Boolean,
    val message: String,
    val response: List<Any>,
    val status: Int
)