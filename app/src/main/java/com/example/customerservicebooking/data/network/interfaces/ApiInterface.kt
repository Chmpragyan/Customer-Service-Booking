package com.example.customerservicebooking.data.network.interfaces

import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.model.BookingRequest
import com.example.customerservicebooking.model.Service
import com.example.customerservicebooking.model.TimeSlot
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent

interface ApiInterface {
    suspend fun getServices(query: String? = null): ApiResponseEvent<List<Service>>
    suspend fun getServiceById(serviceId: String): ApiResponseEvent<Service>
    suspend fun getAvailability(serviceId: String, date: String): ApiResponseEvent<List<TimeSlot>>
    suspend fun createBooking(request: BookingRequest): ApiResponseEvent<Booking>
    suspend fun getBookings(): ApiResponseEvent<List<Booking>>
    suspend fun getBookingById(bookingId: String): ApiResponseEvent<Booking>
}