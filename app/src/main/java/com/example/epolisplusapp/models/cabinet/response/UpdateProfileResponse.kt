package com.example.epolisplusapp.models.cabinet.response

data class UpdateProfileResponse(
    val code: Boolean,
    val message: String,
    val response: List<Any>,
    val status: Int
)
