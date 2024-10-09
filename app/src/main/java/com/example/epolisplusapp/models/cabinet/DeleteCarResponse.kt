package com.example.epolisplusapp.models.cabinet

data class DeleteCarResponse(
    val code: Boolean,
    val message: String,
    val response: List<Any>,
    val status: Int
)