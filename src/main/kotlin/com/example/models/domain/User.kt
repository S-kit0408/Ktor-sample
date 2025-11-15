package com.example.models.domain

import com.example.models.dto.UserResponse

data class User (
    val id: Int,
    val name: String,
    val email: String,
)

fun User.toResponse() = UserResponse(
    id = id,
    name = name,
    email = email,
)