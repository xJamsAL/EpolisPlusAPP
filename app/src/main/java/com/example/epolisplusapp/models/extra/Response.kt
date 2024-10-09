package com.example.epolisplusapp.models.extra

data class Response(
    val info: String,
    val name: String,
    val phone: String,
    val risk: List<Risk>
)