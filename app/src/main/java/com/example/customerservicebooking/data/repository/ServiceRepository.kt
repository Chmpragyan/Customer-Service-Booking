package com.example.customerservicebooking.data.repository

import com.example.customerservicebooking.data.mock.MockApiService

class ServiceRepository(private val apiService: MockApiService) {
    suspend fun getServices(query: String? = null) {
        apiService.getServices(query)
    }

    suspend fun getServiceById(serviceId: String) {
        apiService.getServiceById(serviceId)
    }

    suspend fun getAvailability(serviceId: String, date: String) =
        apiService.getAvailability(serviceId, date)

}