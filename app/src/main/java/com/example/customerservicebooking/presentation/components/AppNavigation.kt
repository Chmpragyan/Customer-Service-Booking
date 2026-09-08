package com.example.customerservicebooking.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.customerservicebooking.presentation.screens.serviceDetail.ServiceDetailScreen
import com.example.customerservicebooking.presentation.screens.serviceDetail.ServiceDetailViewModel
import com.example.customerservicebooking.presentation.screens.services.ServiceListScreen
import com.example.customerservicebooking.presentation.screens.services.ServiceViewModel

@Composable
fun AppNavigation(
    serviceViewModel: ServiceViewModel,
    serviceDetailViewModel: ServiceDetailViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "serviceList") {
        composable("serviceList") {
            ServiceListScreen(
                viewModel = serviceViewModel,
                onServiceClick = { service ->
                    navController.navigate("serviceDetail/${service.id}")
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
                    navController.navigate("bookingConfirm/${service.id}/$date/${slot.startTime}")
                }
            )
        }
        composable(
            route = "bookingConfirm/{serviceId}/{date}/{startTime}",
            arguments = listOf(
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("startTime") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            val date = backStackEntry.arguments?.getString("date")
            val startTime = backStackEntry.arguments?.getString("startTime")
            
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Booking Screen for $serviceId on $date at $startTime")
            }
        }
    }
}
