package com.example.presentation.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val email: String,
    val name: String,
    val password: String ?= null,
    val googleId: String ?= null,
    val avatarUrl: String ?= null,
    val defaultPrivacy: String ?= null,
)

@Serializable
data class LoginRequest(
    val email: String ?= null,
    val password: String ?= null,
    val googleId: String ?= null,
)

@Serializable
data class ChangePasswordRequest(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
)