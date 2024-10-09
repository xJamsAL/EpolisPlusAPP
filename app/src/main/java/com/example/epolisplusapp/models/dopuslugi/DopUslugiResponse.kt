package com.example.epolisplusapp.models.dopuslugi

data class DopUslugiResponse(
    val code: Boolean,
    val message: String,
    val response: Response,
    val status: Int
)