package com.example.epolisplusapp.models.dopuslugi

data class Response(
    val active_service: Boolean,
    val discount_body: String,
    val discount_length: Int,
    val discount_percent: Int,
    val discount_title: String,
    val info: String,
    val name: String,
    val risk: List<DopItems>
)