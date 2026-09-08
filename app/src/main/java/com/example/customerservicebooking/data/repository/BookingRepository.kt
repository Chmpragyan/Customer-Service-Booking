package com.example.customerservicebooking.data.repository

import com.example.customerservicebooking.data.mock.MockApiService
import com.example.customerservicebooking.model.BookingRequest

class BookingRepository(private val apiService: MockApiService) {
    suspend fun createBooking(bookingRequest: BookingRequest){
        apiService.createBooking(bookingRequest)
    }
    suspend fun getMyBookings() = apiService.getBookings()
    suspend fun getBookingDetails(bookingId: String) = apiService.getBookingById(bookingId)
}