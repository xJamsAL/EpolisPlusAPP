package com.example.epolisplusapp.models.emergency

data class EmergencyServiceResponse (
    val info: String,
    val name: String,
    val phone: String,
    val risk: List<EmergencyServiceRisk>
)