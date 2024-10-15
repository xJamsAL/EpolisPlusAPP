package com.example.epolisplusapp.ui.dopservice

import com.example.epolisplusapp.models.cabinet.request.AddCarRequest

interface CarDataListener {
    fun onCarDataReceived(addCarRequest: AddCarRequest)
}