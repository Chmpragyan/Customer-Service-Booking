package com.example.customerservicebooking.presentation.screens.bookingDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.presentation.screens.myBookings.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    bookingId: String?,
    viewModel: BookingDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(bookingId) {
        bookingId?.let { viewModel.loadBooking(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Details") },
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
        ) {
            when (val state = uiState) {
                is BookingDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BookingDetailUiState.Success -> {
                    BookingDetailContent(booking = state.booking)
                }
                is BookingDetailUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { bookingId?.let { viewModel.loadBooking(it) } }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingDetailContent(booking: Booking) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status and Number
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Booking Number", style = MaterialTheme.typography.labelMedium)
                    Text(booking.bookingNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                StatusBadge(status = booking.status)
            }
        }

        // Service Info
        Text("Service Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        DetailRow("Service", booking.serviceName)
        DetailRow("Provider", booking.provider)
        DetailRow("Scheduled Date", booking.scheduledDate)
        DetailRow("Scheduled Time", booking.scheduledTime)
        DetailRow("Total Price", "${booking.currency} ${booking.totalPrice}")

        HorizontalDivider()

        // Customer Info
        Text("Customer Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        DetailRow("Name", booking.customerName)
        DetailRow("Contact", booking.customerContact)
        booking.customerAddress?.let {
            DetailRow("Address", it)
        }

        HorizontalDivider()

        // Meta Info
        Text("Booking Created On", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(booking.createdAt, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}
