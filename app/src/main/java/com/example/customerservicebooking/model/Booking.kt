package com.example.customerservicebooking.model

data class Booking(
    val bookingId: String,
    val bookingNumber: String,
    val serviceId: String,
    val serviceName: String,
    val provider: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val status: BookingStatus,
    val customerName: String,
    val customerContact: String,
    val customerAddress: String?,
    val totalPrice: Double,
    val currency: String,
    val createdAt: String
)