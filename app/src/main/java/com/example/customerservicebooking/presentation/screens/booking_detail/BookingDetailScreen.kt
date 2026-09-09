package com.example.customerservicebooking.presentation.screens.booking_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.customerservicebooking.R
import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.presentation.components.BookingToolbar
import com.example.customerservicebooking.presentation.components.DetailRow
import com.example.customerservicebooking.presentation.components.StatusBadge

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
            BookingToolbar(
                title = stringResource(R.string.booking_details_title),
                showBackArrow = true,
                onBackClick = onBackClick
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
                        Text(
                            text = stringResource(R.string.error_message, state.message),
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { bookingId?.let { viewModel.loadBooking(it) } }) {
                            Text(stringResource(R.string.retry))
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
                    Text(stringResource(R.string.label_booking_number), style = MaterialTheme.typography.labelMedium)
                    Text(booking.bookingNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                StatusBadge(status = booking.status)
            }
        }

        // Service Info
        Text(stringResource(R.string.service_information), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        DetailRow(stringResource(R.string.label_service), booking.serviceName)
        DetailRow(stringResource(R.string.label_provider), booking.provider)
        DetailRow(stringResource(R.string.label_scheduled_date), booking.scheduledDate)
        DetailRow(stringResource(R.string.label_scheduled_time), booking.scheduledTime)
        DetailRow(stringResource(R.string.label_total_price), "${booking.currency} ${booking.totalPrice}")

        HorizontalDivider()

        // Customer Info
        Text(stringResource(R.string.customer_information), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        DetailRow(stringResource(R.string.label_name), booking.customerName)
        DetailRow(stringResource(R.string.label_contact), booking.customerContact)
        booking.customerAddress?.let {
            DetailRow(stringResource(R.string.label_address), it)
        }

        HorizontalDivider()

        // Meta Info
        Text(stringResource(R.string.label_booking_created_on), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(booking.createdAt, style = MaterialTheme.typography.bodySmall)
    }
}
