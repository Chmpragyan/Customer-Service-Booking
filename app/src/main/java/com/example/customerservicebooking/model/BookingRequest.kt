package com.example.customerservicebooking.model

data class BookingRequest(
    val serviceId: String,
    val slotId: String,
    val date: String,
    val startTime: String,
    val customerName: String,
    val customerContact: String,
    val customerAddress: String?
)