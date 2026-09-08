package com.example.customerservicebooking.data.mock

import com.example.customerservicebooking.data.network.interfaces.ApiInterface
import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.model.BookingRequest
import com.example.customerservicebooking.model.Service
import com.example.customerservicebooking.model.TimeSlot
import com.example.customerservicebooking.utils.api.event.ApiError
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent
import com.example.customerservicebooking.utils.enums.BookingStatus
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MockApiService : ApiInterface {

    private val services = listOf(
        Service(
            "1",
            "AC Repair",
            "Maintenance",
            "CoolAir Solutions",
            500.0,
            "NPR",
            60,
            4.5,
            "Professional AC maintenance and repair"
        ),
        Service(
            "2",
            "House Cleaning",
            "Cleaning",
            "Sparkle Clean",
            800.0,
            "NPR",
            120,
            4.8,
            "Deep cleaning for your home"
        ),
        Service(
            "3",
            "Plumbing",
            "Maintenance",
            "QuickFix Plumbers",
            600.0,
            "NPR",
            45,
            4.2,
            "Emergency plumbing services"
        ),
        Service(
            "4",
            "Garden Care",
            "Gardening",
            "Green Thumb",
            400.0,
            "NPR",
            90,
            4.6,
            "Lawn mowing and garden maintenance"
        )
    )

    private val bookings = mutableListOf<Booking>()

    override suspend fun getServices(query: String?): ApiResponseEvent<List<Service>> {
        delay(500)
        val filtered = if (query != null) {
            services.filter { it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true) }
        } else {
            services
        }
        return ApiResponseEvent.Success(filtered)
    }

    override suspend fun getServiceById(serviceId: String): ApiResponseEvent<Service> {
        delay(300)
        val service = services.find { it.id == serviceId }
        return if (service != null) {
            ApiResponseEvent.Success(service)
        } else {
            ApiResponseEvent.Error(ApiError.NotFound)
        }
    }

    override suspend fun getAvailability(serviceId: String, date: String): ApiResponseEvent<List<TimeSlot>> {
        delay(500)
        val slots = listOf(
            TimeSlot("s1", date, "09:00", "10:00", true),
            TimeSlot("s2", date, "10:30", "11:30", true),
            TimeSlot("s3", date, "13:00", "14:00", false),
            TimeSlot("s4", date, "15:30", "16:30", true)
        )
        return ApiResponseEvent.Success(slots)
    }

    override suspend fun createBooking(request: BookingRequest): ApiResponseEvent<Booking> {
        delay(1000)
        val service = services.find { it.id == request.serviceId }
            ?: return ApiResponseEvent.Error(ApiError.Conflict("Invalid service"))

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newBooking = Booking(
            bookingId = UUID.randomUUID().toString(),
            bookingNumber = "BK${System.currentTimeMillis().toString().takeLast(6)}",
            serviceId = service.id,
            serviceName = service.name,
            provider = service.provider,
            scheduledDate = request.date,
            scheduledTime = request.startTime,
            status = BookingStatus.PENDING,
            customerName = request.customerName,
            customerContact = request.customerContact,
            customerAddress = request.customerAddress,
            totalPrice = service.price,
            currency = service.currency,
            createdAt = sdf.format(Date())
        )
        bookings.add(newBooking)
        return ApiResponseEvent.Success(newBooking)
    }

    override suspend fun getBookings(): ApiResponseEvent<List<Booking>> {
        delay(500)
        return ApiResponseEvent.Success(bookings.toList())
    }

    override suspend fun getBookingById(bookingId: String): ApiResponseEvent<Booking> {
        delay(300)
        val booking = bookings.find { it.bookingId == bookingId }
        return if (booking != null) {
            ApiResponseEvent.Success(booking)
        } else {
            ApiResponseEvent.Error(ApiError.NotFound)
        }
    }
}