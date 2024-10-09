package com.example.epolisplusapp.models.cabinet

data class DeleteProfileRequest (
    val phone: String
)
data class DeleteProfileResponse(
    val code: Boolean,
    val message: String,
    val response: List<Any>,
    val status: Int
)