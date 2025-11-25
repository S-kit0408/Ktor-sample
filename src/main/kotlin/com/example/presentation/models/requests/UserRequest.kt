package com.example.presentation.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val email: String,
    val name: String,
    val avatarUrl: String? = null,
    val defaultPrivacySetting: String? = null  // "public", "friends", "private"
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val avatarUrl: String? = null,
    val defaultPrivacySetting: String? = null  // "public", "friends", "private"
)