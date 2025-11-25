package com.example.presentation.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
) {
    companion object {
        fun <T> success(data: T, message: String? = null): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message
            )
        }

        fun <T> error(message: String, error: String? = null): ApiResponse<T> {
            return ApiResponse(
                success = false,
                message = message,
                error = error
            )
        }
    }
}


@Serializable
data class ErrorResponse(
    val error: String,
    val message: String
)