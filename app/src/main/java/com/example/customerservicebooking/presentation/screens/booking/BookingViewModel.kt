package com.example.customerservicebooking.presentation.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerservicebooking.data.repository.ServiceRepository
import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.model.BookingRequest
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun confirmBooking(
        serviceId: String,
        date: String,
        startTime: String,
        customerName: String,
        customerContact: String,
        customerAddress: String
    ) {
        if (customerName.isBlank() || customerContact.isBlank()) {
            _uiState.value = BookingUiState.Error("Name and Contact are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = BookingUiState.Submitting
            val request = BookingRequest(
                serviceId = serviceId,
                slotId = "dummy_slot_id",
                date = date,
                startTime = startTime,
                customerName = customerName,
                customerContact = customerContact,
                customerAddress = customerAddress.ifBlank { null }
            )

            when (val result = repository.createBooking(request)) {
                is ApiResponseEvent.Success -> {
                    _uiState.value = BookingUiState.Success(result.data)
                }
                is ApiResponseEvent.Error -> {
                    _uiState.value = BookingUiState.Error(result.error.message)
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = BookingUiState.Idle
    }
}

sealed class BookingUiState {
    object Idle : BookingUiState()
    object Submitting : BookingUiState()
    data class Success(val booking: Booking) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}
