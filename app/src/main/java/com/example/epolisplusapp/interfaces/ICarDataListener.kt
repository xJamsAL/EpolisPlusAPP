package com.example.epolisplusapp.interfaces

import com.example.epolisplusapp.models.cabinet.request.AddCarRequest

interface ICarDataListener {
    fun onCarDataReceived(addCarRequest: AddCarRequest)
}