package com.example.epolisplusapp.models.partners

data class PartnersResponse(
    val code: Boolean,
    val message: String,
    val response: List<Response>,
    val status: Int
)