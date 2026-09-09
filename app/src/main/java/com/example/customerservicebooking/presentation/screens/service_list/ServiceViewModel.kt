package com.example.customerservicebooking.presentation.screens.service_list

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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadServices()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadServices(_searchQuery.value, isRefreshing = true)
            _isRefreshing.value = false
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        loadServices(newQuery)
    }

    fun loadServices(query: String? = _searchQuery.value, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (!isRefreshing) {
                _servicesState.value = ServiceUiState.Loading
            }
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
