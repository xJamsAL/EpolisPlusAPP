package com.example.epolisplusapp.models.cabinet

data class AddCarRequest(
    val BODY_NUMBER: String,
    val ENGINE_NUMBER: String,
    val FIRST_NAME: String,
    val FY: String,
    val GOV_NUMBER: String,
    val INN: Any,
    val ISSUE_YEAR: String,
    val LAST_NAME: String,
    val MARKA_ID: String,
    val MIDDLE_NAME: String,
    val MODEL_ID: Any,
    val MODEL_NAME: String,
    val ORGNAME: String,
    val PINFL: String,
    val TECH_NUMBER: String,
    val TECH_PASSPORT_ISSUE_DATE: String,
    val TECH_SERIYA: String,
    val USE_TERRITORY: String,
    val VEHICLE_TYPE_ID: String
)