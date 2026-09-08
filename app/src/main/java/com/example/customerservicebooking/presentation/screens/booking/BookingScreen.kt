package com.example.customerservicebooking.presentation.screens.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.customerservicebooking.model.Booking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    serviceId: String,
    serviceName: String,
    date: String,
    startTime: String,
    price: String,
    viewModel: BookingViewModel,
    onBackClick: () -> Unit,
    onBookingSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var customerName by remember { mutableStateOf("") }
    var customerContact by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }
    
    var nameError by remember { mutableStateOf(false) }
    var contactError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Booking") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding()
        ) {
            when (val state = uiState) {
                is BookingUiState.Success -> {
                    BookingSuccessContent(state.booking, onBookingSuccess)
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Summary Section
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Booking Summary", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                SummaryRow("Service", serviceName)
                                SummaryRow("Date", date)
                                SummaryRow("Time", startTime)
                                SummaryRow("Total Price", price)
                            }
                        }

                        HorizontalDivider()

                        // Form Section
                        Text("Customer Information", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { 
                                customerName = it
                                nameError = false
                            },
                            label = { Text("Full Name *") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nameError,
                            supportingText = { if (nameError) Text("Required") }
                        )

                        OutlinedTextField(
                            value = customerContact,
                            onValueChange = { 
                                customerContact = it
                                contactError = false
                            },
                            label = { Text("Contact Number *") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = contactError,
                            supportingText = { if (contactError) Text("Required") }
                        )

                        OutlinedTextField(
                            value = customerAddress,
                            onValueChange = { customerAddress = it },
                            label = { Text("Address (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        if (state is BookingUiState.Error) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                nameError = customerName.isBlank()
                                contactError = customerContact.isBlank()
                                
                                if (!nameError && !contactError) {
                                    viewModel.confirmBooking(
                                        serviceId = serviceId,
                                        date = date,
                                        startTime = startTime,
                                        customerName = customerName,
                                        customerContact = customerContact,
                                        customerAddress = customerAddress
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = state !is BookingUiState.Submitting
                        ) {
                            if (state is BookingUiState.Submitting) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            } else {
                                Text("Confirm Booking")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun BookingSuccessContent(booking: Booking, onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Booking Confirmed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Booking ID: ${booking.bookingNumber}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Home")
        }
    }
}
