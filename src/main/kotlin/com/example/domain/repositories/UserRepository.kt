package com.example.domain.repositories

import com.example.domain.models.User
import com.example.domain.models.UserId

interface UserRepository {

    suspend fun findById(id: UserId): User?
    suspend fun findByEmail(email: String): User?
    suspend fun findAll(): List<User>
    suspend fun save(user: User): User
    suspend fun delete(id: UserId): Boolean
    suspend fun existsByEmail(email: String): Boolean
}