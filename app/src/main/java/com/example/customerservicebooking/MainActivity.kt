package com.example.customerservicebooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.customerservicebooking.data.mock.MockApiService
import com.example.customerservicebooking.data.repository.ServiceRepository
import com.example.customerservicebooking.presentation.components.AppNavigation
import com.example.customerservicebooking.presentation.screens.serviceScreen.ServiceViewModel
import com.example.customerservicebooking.ui.theme.CustomerServiceBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual DI for simplicity in this mock setup
        val apiService = MockApiService()
        val repository = ServiceRepository(apiService)
        val viewModel = ServiceViewModel(repository)

        enableEdgeToEdge()
        setContent {
            CustomerServiceBookingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel)
                }
            }
        }
    }
}
