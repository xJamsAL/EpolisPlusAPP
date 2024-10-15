package com.example.epolisplusapp.models.cabinet.response

data class CheckCarResponse(
    val code: Boolean,
    val message: String,
    val response: AddUserCarResponse,
    val status: Int
)