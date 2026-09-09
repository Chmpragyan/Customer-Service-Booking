package com.example.customerservicebooking.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.customerservicebooking.presentation.navigation.Screen
import com.example.customerservicebooking.presentation.screens.booking.BookingScreen
import com.example.customerservicebooking.presentation.screens.booking.BookingViewModel
import com.example.customerservicebooking.presentation.screens.booking_detail.BookingDetailScreen
import com.example.customerservicebooking.presentation.screens.booking_detail.BookingDetailViewModel
import com.example.customerservicebooking.presentation.screens.my_booking.MyBookingScreen
import com.example.customerservicebooking.presentation.screens.my_booking.MyBookingViewModel
import com.example.customerservicebooking.presentation.screens.service_detail.ServiceDetailScreen
import com.example.customerservicebooking.presentation.screens.service_detail.ServiceDetailViewModel
import com.example.customerservicebooking.presentation.screens.service_list.ServiceListScreen
import com.example.customerservicebooking.presentation.screens.service_list.ServiceViewModel

@Composable
fun AppNavigation(
    serviceViewModel: ServiceViewModel,
    serviceDetailViewModel: ServiceDetailViewModel,
    bookingViewModel: BookingViewModel,
    myBookingViewModel: MyBookingViewModel,
    bookingDetailViewModel: BookingDetailViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = Screen.ServiceList.route
    ) {
        composable(Screen.ServiceList.route) {
            ServiceListScreen(
                viewModel = serviceViewModel,
                onServiceClick = { service ->
                    navController.navigate(Screen.ServiceDetail.createRoute(service.id))
                },
                onMyBookingsClick = {
                    navController.navigate(Screen.MyBookings.route)
                }
            )
        }
        
        composable(
            route = Screen.ServiceDetail.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            ServiceDetailScreen(
                serviceId = serviceId,
                viewModel = serviceDetailViewModel,
                onBackClick = { navController.popBackStack() },
                onBookNowClick = { service, date, slot ->
                    navController.navigate(
                        Screen.BookingForm.createRoute(
                            serviceId = service.id,
                            serviceName = service.name,
                            date = date,
                            slotId = slot.id,
                            startTime = slot.startTime,
                            price = "${service.currency} ${service.price}"
                        )
                    )
                }
            )
        }
        
        composable(
            route = Screen.BookingForm.route,
            arguments = listOf(
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("slotId") { type = NavType.StringType },
                navArgument("startTime") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val slotId = backStackEntry.arguments?.getString("slotId") ?: ""
            val startTime = backStackEntry.arguments?.getString("startTime") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: ""

            BookingScreen(
                serviceId = serviceId,
                serviceName = serviceName,
                date = date,
                slotId = slotId,
                startTime = startTime,
                price = price,
                viewModel = bookingViewModel,
                onBackClick = { navController.popBackStack() },
                onBookingSuccess = {
                    bookingViewModel.resetState()
                    navController.navigate(Screen.MyBookings.route) {
                        popUpTo(Screen.ServiceList.route) { inclusive = false }
                    }
                }
            )
        }
        
        composable(Screen.MyBookings.route) {
            MyBookingScreen(
                viewModel = myBookingViewModel,
                onBackClick = { navController.popBackStack() },
                onBookingClick = { booking ->
                    navController.navigate(Screen.BookingDetail.createRoute(booking.bookingId))
                }
            )
        }
        
        composable(
            route = Screen.BookingDetail.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId")
            BookingDetailScreen(
                bookingId = bookingId,
                viewModel = bookingDetailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
