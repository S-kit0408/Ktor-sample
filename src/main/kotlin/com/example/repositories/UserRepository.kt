package com.example.repositories

import com.example.models.domain.User

interface UserRepository {
    suspend fun findById(id: Int): User?
}

class UserRepositoryImpl : UserRepository {
    private val users = mutableMapOf<Int, User>()

    init {
        // テスト用データを初期化
        users[1] = User(1, "太郎", "taro@example.com")
        users[2] = User(2, "次郎", "jiro@example.com")
        users[3] = User(3, "三郎", "saburo@example.com")
    }

    override suspend fun findById(id: Int): User? {
        return users[id]
    }
}