package com.example.customerservicebooking.presentation.screens.bookingDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerservicebooking.data.repository.ServiceRepository
import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingDetailViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<BookingDetailUiState>(BookingDetailUiState.Loading)
    val uiState: StateFlow<BookingDetailUiState> = _uiState.asStateFlow()

    fun loadBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = BookingDetailUiState.Loading
            when (val result = repository.getBookingById(bookingId)) {
                is ApiResponseEvent.Success -> {
                    _uiState.value = BookingDetailUiState.Success(result.data)
                }
                is ApiResponseEvent.Error -> {
                    _uiState.value = BookingDetailUiState.Error(result.error.message)
                }
            }
        }
    }
}

sealed class BookingDetailUiState {
    object Loading : BookingDetailUiState()
    data class Success(val booking: Booking) : BookingDetailUiState()
    data class Error(val message: String) : BookingDetailUiState()
}
