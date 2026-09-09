package com.example.customerservicebooking.presentation.navigation

sealed class Screen(val route: String) {
    object ServiceList : Screen("service_list")
    object MyBookings : Screen("my_bookings")
    
    object ServiceDetail : Screen("service_detail/{serviceId}") {
        fun createRoute(serviceId: String) = "service_detail/$serviceId"
    }
    
    object BookingForm : Screen("booking/{serviceId}/{serviceName}/{date}/{slotId}/{startTime}/{price}") {
        fun createRoute(
            serviceId: String, 
            serviceName: String, 
            date: String, 
            slotId: String, 
            startTime: String, 
            price: String
        ) = "booking/$serviceId/$serviceName/$date/$slotId/$startTime/$price"
    }
    
    object BookingDetail : Screen("booking_detail/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_detail/$bookingId"
    }
}
