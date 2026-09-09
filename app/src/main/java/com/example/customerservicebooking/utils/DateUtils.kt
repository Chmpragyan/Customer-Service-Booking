package com.example.customerservicebooking.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    
    private const val API_DATE_FORMAT = "yyyy-MM-dd"
    private const val DISPLAY_DATE_FORMAT = "MMM dd\nEEE"

    /**
     * Converts an API date string (yyyy-MM-dd) to a display format (e.g., May 20, Mon).
     */
    fun formatToDisplay(date: String): String {
        return try {
            val dateObj = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault()).parse(date)
            SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()).format(dateObj!!)
        } catch (e: Exception) {
            date
        }
    }
}
