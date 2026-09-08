package com.example.customerservicebooking.data.repository

import com.example.customerservicebooking.data.mock.MockApiService
import com.example.customerservicebooking.model.BookingRequest

class ServiceRepository(private val apiService: MockApiService) {
    suspend fun getServices(query: String? = null) = apiService.getServices(query)

    suspend fun getServiceById(serviceId: String) = apiService.getServiceById(serviceId)

    suspend fun getAvailability(serviceId: String, date: String) =
        apiService.getAvailability(serviceId, date)

    suspend fun createBooking(request: BookingRequest) =
        apiService.createBooking(request)

    suspend fun getBookings() = apiService.getBookings()

    suspend fun getBookingById(bookingId: String) = apiService.getBookingById(bookingId)

}