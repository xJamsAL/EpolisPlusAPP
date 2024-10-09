package com.example.epolisplusapp.models.cabinet

data class CheckCarRequest(
    val govNumber: String,
    val techPassportNumber: String,
    val techPassportSeria: String
)