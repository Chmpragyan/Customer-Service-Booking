package com.example.customerservicebooking.model

data class TimeSlot(
    val id: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isAvailable: Boolean
)