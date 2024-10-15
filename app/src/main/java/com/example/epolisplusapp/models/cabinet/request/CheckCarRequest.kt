package com.example.epolisplusapp.models.cabinet.request

data class CheckCarRequest(
    val govNumber: String,
    val techPassportNumber: String,
    val techPassportSeria: String
)