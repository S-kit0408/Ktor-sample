package com.example.presentation.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val email: String,
    val name: String
)

@Serializable
data class UpdateUserRequest(
    val name: String
)