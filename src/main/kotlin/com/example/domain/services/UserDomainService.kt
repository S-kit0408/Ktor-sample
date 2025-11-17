package com.example.domain.services

import com.example.domain.models.Email
import com.example.domain.repositories.UserRepository

class UserDomainService (
    private val userRepository: UserRepository
) {
    suspend fun checkEmailDuplicatoin(email: String) {
        if (!Email.isValid(email)) {
            throw IllegalArgumentException("Invalid email format: $email")
        }

        if (userRepository.existsByEmail(email)) {
            throw EmailAlreadyExistsException("Email already exists: \$email")
        }
    }

    fun validateUserName(name: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }

        if (name.length !in 2..50) {
            throw IllegalArgumentException("Name must be between 2 and 50 characters")
        }
    }
}

class EmailAlreadyExistsException(message: String) : Exception(message)