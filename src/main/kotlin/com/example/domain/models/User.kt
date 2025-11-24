package com.example.domain.models

import kotlinx.datetime.Instant
import com.github.f4b6a3.ulid.UlidCreator

data class User(
    val id: UserId,
    val email: Email,
    val name: String,
    val avatarUrl: String?,
    val defaultPrivacySetting: PrivacySetting,
    val lastLoginAt : Instant?,
    val createdAt: Instant,
    val updatedAt: Instant
) {

    fun hasValidName(): Boolean {
        return name.isNotBlank() && name.length in 2..100
    }

    fun updateName(newName: String): User {
        require(newName.isNotBlank()) { "Name cannot be blank" }
        require(newName.length in 2..100) { "Name must be between 2 and 50 characters" }
        return copy(name = newName, updatedAt = Instant.fromEpochMilliseconds(System.currentTimeMillis()))
    }

    fun updateAvatarUrl(newAvatarUrl: String?): User {
        newAvatarUrl?.let {
            require(it.length <= 500) { "Avatar url cannot be greater than 500" }
        }
        return copy(avatarUrl = newAvatarUrl, updatedAt = Instant.fromEpochMilliseconds(System.currentTimeMillis()))
    }

    fun updatePrivacySetting(newPrivacySetting: PrivacySetting): User {
        return copy(defaultPrivacySetting = newPrivacySetting, updatedAt = Instant.fromEpochMilliseconds(System.currentTimeMillis()))
    }

    fun recordLogin(loginTime: Instant): User {
        return copy(lastLoginAt = loginTime)
    }

    companion object {
        fun create(
            email: String,
            name: String,
            avatarUrl: String? = null,
            defaultPrivacySetting: PrivacySetting = PrivacySetting.PRIVATE,
            now: Instant
        ): User {
            return User(
                id = UserId.generate(),
                email = Email(email),
                name = name,
                avatarUrl = avatarUrl,
                defaultPrivacySetting = defaultPrivacySetting,
                lastLoginAt = null,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}

enum class PrivacySetting {
    PUBLIC,
    FRIENDS,
    PRIVATE;

    companion object {
        fun fromString(value: String): PrivacySetting {
            return valueOf(value.uppercase())
        }
    }
}


@JvmInline
value class UserId(val value: String) {
    init {
        require(isValid(value)) { "Invalid ULID format: $value" }
    }

    companion object {

        fun generate(): UserId {
            return UserId(UlidCreator.getUlid().toString())
        }

        fun of(value: String): UserId {
            require(value.isNotBlank()) { "UserId cannot be blank" }
            return UserId(value)
        }

        private fun isValid(value: String): Boolean {
            // ULIDは26桁、0-9A-Zのみ
            return value.length == 26 && value.all { it in '0'..'9' || it in 'A'..'Z' }
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