package com.example.customerservicebooking.presentation.screens.service_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.customerservicebooking.R
import com.example.customerservicebooking.model.Service
import com.example.customerservicebooking.model.TimeSlot
import com.example.customerservicebooking.presentation.components.BookingToolbar
import com.example.customerservicebooking.presentation.components.InfoChip
import com.example.customerservicebooking.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    serviceId: String?,
    viewModel: ServiceDetailViewModel,
    onBackClick: () -> Unit,
    onBookNowClick: (Service, String, TimeSlot) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val availabilityState by viewModel.availabilityState.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    var selectedSlot by remember { mutableStateOf<TimeSlot?>(null) }

    LaunchedEffect(serviceId) {
        serviceId?.let { viewModel.loadService(it) }
    }

    Scaffold(
        topBar = {
            BookingToolbar(
                title = stringResource(R.string.service_details),
                showBackArrow = true,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            if (uiState is ServiceDetailUiState.Success) {
                val service = (uiState as ServiceDetailUiState.Success).service
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Button(
                        onClick = {
                            if (selectedDate != null && selectedSlot != null) {
                                onBookNowClick(service, selectedDate!!, selectedSlot!!)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedDate != null && selectedSlot != null
                    ) {
                        Text(stringResource(R.string.continue_to_booking))
                    }
                }
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ServiceDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ServiceDetailUiState.Success -> {
                val service = state.service
                ServiceDetailContent(
                    service = service,
                    availabilityState = availabilityState,
                    selectedDate = selectedDate,
                    selectedSlot = selectedSlot,
                    onDateSelected = {
                        selectedSlot = null // Reset slot when date changes
                        viewModel.selectDate(service.id, it)
                    },
                    onSlotSelected = { selectedSlot = it },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ServiceDetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.error_message, state.message),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceDetailContent(
    service: Service,
    availabilityState: AvailabilityUiState,
    selectedDate: String?,
    selectedSlot: TimeSlot?,
    onDateSelected: (String) -> Unit,
    onSlotSelected: (TimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Service Header
        Text(
            text = service.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = service.category,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoChip(label = stringResource(R.string.label_provider), value = service.provider)
            InfoChip(
                label = stringResource(R.string.label_rating),
                value = stringResource(R.string.rating_format, service.rating)
            )
            InfoChip(
                label = stringResource(R.string.label_duration),
                value = stringResource(R.string.duration_mins_format, service.durationMinutes)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Section
        Text(
            text = "${service.currency} ${service.price}",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = stringResource(R.string.label_description),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = service.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Date Selection
        Text(
            text = stringResource(R.string.label_select_date),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        // Use available dates from service model
        val dates = service.availableDates
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(dates) { date ->
                DateItem(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = { onDateSelected(date) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Availability Section
        Text(
            text = stringResource(R.string.label_available_slots),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        when (availabilityState) {
            is AvailabilityUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is AvailabilityUiState.Success -> {
                if (availabilityState.slots.isEmpty()) {
                    Text(stringResource(R.string.no_slots_available))
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availabilityState.slots.forEach { slot ->
                            SlotItem(
                                slot = slot,
                                isSelected = slot == selectedSlot,
                                onClick = { if (slot.isAvailable) onSlotSelected(slot) }
                            )
                        }
                    }
                }
            }
            is AvailabilityUiState.Error -> {
                Text(text = availabilityState.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
        
        Spacer(modifier = Modifier.height(80.dp)) // Extra space for bottom bar
    }
}

@Composable
fun DateItem(date: String, isSelected: Boolean, onClick: () -> Unit) {
    val displayDate = DateUtils.formatToDisplay(date)

    Card(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)) else null
    ) {
        Text(
            text = displayDate,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun SlotItem(slot: TimeSlot, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        !slot.isAvailable -> Color.LightGray.copy(alpha = 0.5f)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    
    val textColor = when {
        !slot.isAvailable -> Color.Gray
        isSelected -> Color.White
        else -> Color.Black
    }

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable(enabled = slot.isAvailable, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (!isSelected && slot.isAvailable) BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Text(
            text = slot.startTime,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        content = { content() }
    )
}
