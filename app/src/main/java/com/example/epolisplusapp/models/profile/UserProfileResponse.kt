package com.example.epolisplusapp.models.profile

data class UserProfileData(
    val phone: String,
    val full_name: String,
    val car_info: List<CarInfo>
)

data class CarInfo(
    val TECH_SERIYA: String,
    val TECH_NUMBER: String,
    val GOV_NUMBER: String,
    val MODEL_NAME: String,
    val ORGNAME: String,
    val ISSUE_YEAR: String
)
