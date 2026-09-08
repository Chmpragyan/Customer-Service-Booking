package com.example.customerservicebooking.presentation.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customerservicebooking.data.repository.ServiceRepository
import com.example.customerservicebooking.model.Service
import com.example.customerservicebooking.utils.api.event.ApiResponseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _servicesState = MutableStateFlow<ServiceUiState>(ServiceUiState.Loading)
    val servicesState: StateFlow<ServiceUiState> = _servicesState.asStateFlow()

    init {
        loadServices()
    }

    fun loadServices(query: String? = null) {
        viewModelScope.launch {
            _servicesState.value = ServiceUiState.Loading
            when (val result = repository.getServices(query)) {
                is ApiResponseEvent.Success -> {
                    _servicesState.value = ServiceUiState.Success(result.data)
                }

                is ApiResponseEvent.Error -> {
                    _servicesState.value = ServiceUiState.Error(result.error.message)
                }
            }
        }
    }
}

sealed class ServiceUiState {
    object Loading : ServiceUiState()
    data class Success(val services: List<Service>) : ServiceUiState()
    data class Error(val message: String) : ServiceUiState()
}
