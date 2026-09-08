package com.example.customerservicebooking.presentation.screens.serviceDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerservicebooking.data.repository.ServiceRepository
import com.example.customerservicebooking.model.Service
import com.example.customerservicebooking.model.TimeSlot
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceDetailViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ServiceDetailUiState>(ServiceDetailUiState.Loading)
    val uiState: StateFlow<ServiceDetailUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    private val _availabilityState = MutableStateFlow<AvailabilityUiState>(AvailabilityUiState.Initial)
    val availabilityState: StateFlow<AvailabilityUiState> = _availabilityState.asStateFlow()

    fun loadService(serviceId: String) {
        viewModelScope.launch {
            _uiState.value = ServiceDetailUiState.Loading
            when (val result = repository.getServiceById(serviceId)) {
                is ApiResponseEvent.Success -> {
                    val service = result.data
                    _uiState.value = ServiceDetailUiState.Success(service)
                    
                    // Automatically select the first available date and load its slots
                    service.availableDates.firstOrNull()?.let { firstDate ->
                        selectDate(service.id, firstDate)
                    }
                }
                is ApiResponseEvent.Error -> {
                    _uiState.value = ServiceDetailUiState.Error(result.error.message)
                }
            }
        }
    }

    fun selectDate(serviceId: String, date: String) {
        _selectedDate.value = date
        loadAvailability(serviceId, date)
    }

    private fun loadAvailability(serviceId: String, date: String) {
        viewModelScope.launch {
            _availabilityState.value = AvailabilityUiState.Loading
            when (val result = repository.getAvailability(serviceId, date)) {
                is ApiResponseEvent.Success -> {
                    _availabilityState.value = AvailabilityUiState.Success(result.data)
                }
                is ApiResponseEvent.Error -> {
                    _availabilityState.value = AvailabilityUiState.Error(result.error.message)
                }
            }
        }
    }
}

sealed class ServiceDetailUiState {
    object Loading : ServiceDetailUiState()
    data class Success(val service: Service) : ServiceDetailUiState()
    data class Error(val message: String) : ServiceDetailUiState()
}

sealed class AvailabilityUiState {
    object Initial : AvailabilityUiState()
    object Loading : AvailabilityUiState()
    data class Success(val slots: List<TimeSlot>) : AvailabilityUiState()
    data class Error(val message: String) : AvailabilityUiState()
}
