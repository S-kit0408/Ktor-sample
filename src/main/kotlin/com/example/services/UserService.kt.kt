package com.example.services

import com.example.models.dto.UserResponse
import com.example.models.domain.toResponse
import com.example.repositories.UserRepository

class UserService(
    private val userRepository: UserRepository
) {
    suspend fun getUserById(id: Int): Result<UserResponse> {

        if (id <= 0) return Result.failure(IllegalArgumentException("IDは1以上である必要があります"))

        val user = userRepository.findById(id)
            ?: return Result.failure(NoSuchElementException("ユーザーが見つかりません"))

//        return Result.success(UserResponse(id = user.id, name = user.name, email = user.email))
        return Result.success(user.toResponse())
    }
}