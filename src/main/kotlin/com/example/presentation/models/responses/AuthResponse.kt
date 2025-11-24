package com.example.presentation.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: UserResponse,
    val authMethods: List<AuthMethodResponse>,
    val message: String
)

@Serializable
data class AuthMethodResponse(
    val id: String,
    val authType: String,
    val oauthProvider: String? = null,
    val createdAt: String
)

@Serializable
data class UserWithAuthResponse(
    val user: UserResponse,
    val authMethods: List<AuthMethodResponse>
)