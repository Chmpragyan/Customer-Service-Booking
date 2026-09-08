package com.example.customerservicebooking.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.customerservicebooking.presentation.screens.detailScreen.ServiceDetailScreen
import com.example.customerservicebooking.presentation.screens.serviceScreen.ServiceListScreen
import com.example.customerservicebooking.presentation.screens.serviceScreen.ServiceViewModel

@Composable
fun AppNavigation(viewModel: ServiceViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "serviceList") {
        composable("serviceList") {
            ServiceListScreen(
                viewModel = viewModel,
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
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
