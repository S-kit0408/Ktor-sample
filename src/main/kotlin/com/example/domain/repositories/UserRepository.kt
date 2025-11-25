package com.example.domain.repositories

import com.example.domain.models.User

interface UserRepository {
    suspend fun findById(id: String): User?
    suspend fun findByClerkUserId(clerkUserId: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun create(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: String): Boolean
    suspend fun list(): List<User>
}