package com.example.customerservicebooking.presentation.screens.my_booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerservicebooking.data.repository.ServiceRepository
import com.example.customerservicebooking.model.Booking
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyBookingViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MyBookingUiState>(MyBookingUiState.Loading)
    val uiState: StateFlow<MyBookingUiState> = _uiState.asStateFlow()

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = MyBookingUiState.Loading
            when (val result = repository.getBookings()) {
                is ApiResponseEvent.Success -> {
                    _uiState.value = MyBookingUiState.Success(result.data)
                }
                is ApiResponseEvent.Error -> {
                    _uiState.value = MyBookingUiState.Error(result.error.message)
                }
            }
        }
    }
}

sealed class MyBookingUiState {
    object Loading : MyBookingUiState()
    data class Success(val bookings: List<Booking>) : MyBookingUiState()
    data class Error(val message: String) : MyBookingUiState()
}
