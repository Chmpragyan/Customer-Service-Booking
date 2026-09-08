package com.example.customerservicebooking.utils.api.event

sealed class ApiResponseEvent<out T> {
    data class Success<T>(val data: T) : ApiResponseEvent<T>()
    data class Error(val error: ApiError) : ApiResponseEvent<Nothing>()
}

sealed class ApiError(val message: String) {
    data class Validation(val fieldErrors: Map<String, String>) : ApiError("Validation failed")
    object NotFound : ApiError("Not found")
    data class Conflict(val reason: String) : ApiError(reason)
    data class Server(val code: Int, val reason: String) : ApiError(reason)
    data class Network(val reason: String) : ApiError(reason)
}
