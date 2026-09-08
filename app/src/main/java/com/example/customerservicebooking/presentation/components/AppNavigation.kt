package com.example.customerservicebooking.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.customerservicebooking.presentation.screens.booking.BookingScreen
import com.example.customerservicebooking.presentation.screens.booking.BookingViewModel
import com.example.customerservicebooking.presentation.screens.bookingDetail.BookingDetailScreen
import com.example.customerservicebooking.presentation.screens.bookingDetail.BookingDetailViewModel
import com.example.customerservicebooking.presentation.screens.myBookings.MyBookingScreen
import com.example.customerservicebooking.presentation.screens.myBookings.MyBookingViewModel
import com.example.customerservicebooking.presentation.screens.serviceDetail.ServiceDetailScreen
import com.example.customerservicebooking.presentation.screens.serviceDetail.ServiceDetailViewModel
import com.example.customerservicebooking.presentation.screens.services.ServiceListScreen
import com.example.customerservicebooking.presentation.screens.services.ServiceViewModel

@Composable
fun AppNavigation(
    serviceViewModel: ServiceViewModel,
    serviceDetailViewModel: ServiceDetailViewModel,
    bookingViewModel: BookingViewModel,
    myBookingViewModel: MyBookingViewModel,
    bookingDetailViewModel: BookingDetailViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "serviceList") {
        composable("serviceList") {
            ServiceListScreen(
                viewModel = serviceViewModel,
                onServiceClick = { service ->
                    navController.navigate("serviceDetail/${service.id}")
                },
                onMyBookingsClick = {
                    navController.navigate("myBookings")
                }
            )
        }
        composable(
            route = "serviceDetail/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            ServiceDetailScreen(
                serviceId = serviceId,
                viewModel = serviceDetailViewModel,
                onBackClick = { navController.popBackStack() },
                onBookNowClick = { service, date, slot ->
                    navController.navigate("booking/${service.id}/${service.name}/${date}/${slot.id}/${slot.startTime}/${service.currency} ${service.price}")
                }
            )
        }
        composable(
            route = "booking/{serviceId}/{serviceName}/{date}/{slotId}/{startTime}/{price}",
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
                    navController.navigate("myBookings") {
                        popUpTo("serviceList") { inclusive = false }
                    }
                }
            )
        }
        composable("myBookings") {
            MyBookingScreen(
                viewModel = myBookingViewModel,
                onBackClick = { navController.popBackStack() },
                onBookingClick = { booking ->
                    navController.navigate("bookingDetail/${booking.bookingId}")
                }
            )
        }
        composable(
            route = "bookingDetail/{bookingId}",
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
