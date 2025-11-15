package com.example.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetUserRequest(
    val id: Int,
)

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String
)
