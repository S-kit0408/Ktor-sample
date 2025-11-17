package com.example.domain.models

import kotlinx.datetime.Instant

data class User(
    val id: UserId,
    val email: Email,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    fun hasValidName(): Boolean {
        return name.isNotBlank() && name.length in 2..50
    }

    fun updateName(newName: String): User {
        require(newName.isNotBlank()) { "Name cannot be blank" }
        require(newName.length in 2..50) { "Name must be between 2 and 50 characters" }
        return copy(name = newName)
    }

    companion object {
        fun create(
            email: String,
            name: String,
            now: Instant
        ): User {
            return User(
                id = UserId.generate(),
                email = Email(email),
                name = name,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}


@JvmInline
value class UserId(val value: String) {
    companion object {
        fun generate(): UserId {
            return UserId(java.util.UUID.randomUUID().toString())
        }

        fun of(value: String): UserId {
            require(value.isNotBlank()) { "UserId cannot be blank" }
            return UserId(value)
        }
    }
}


@JvmInline
value class Email(val value: String) {
    init {
        require(isValid(value)) { "Invalid email format: $value" }
    }

    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

        fun isValid(email: String): Boolean {
            return email.matches(EMAIL_REGEX)
        }
    }
}