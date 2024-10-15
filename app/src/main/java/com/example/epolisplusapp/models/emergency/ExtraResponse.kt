package com.example.epolisplusapp.models.emergency

data class ExtraResponse(
    val code: Boolean,
    val message: String,
    val response: EmergencyServiceResponse,
    val status: Int
)