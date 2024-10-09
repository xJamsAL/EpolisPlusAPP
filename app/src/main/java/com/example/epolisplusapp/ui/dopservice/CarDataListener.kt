package com.example.epolisplusapp.ui.dopservice

import com.example.epolisplusapp.models.cabinet.AddCarRequest

interface CarDataListener {
    fun onCarDataReceived(addCarRequest: AddCarRequest)
}