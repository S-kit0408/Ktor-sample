package com.example.infrastructure.database.repositories

import com.example.domain.models.Email
import com.example.domain.models.User
import com.example.domain.models.UserId
import com.example.domain.repositories.UserRepository
import kotlinx.datetime.Clock
import java.util.concurrent.ConcurrentHashMap

// テスト用
class InMemoryUserRepository : UserRepository {

    private val users = ConcurrentHashMap<String, User>()

    init {
        // サンプルデータ
        val now = Clock.System.now()

        val user1 = User(
            id = UserId.of("1"),
            email = Email("taro@example.com"),
            name = "太郎",
            createdAt = now,
            updatedAt = now
        )

        val user2 = User(
            id = UserId.of("2"),
            email = Email("jiro@example.com"),
            name = "次郎",
            createdAt = now,
            updatedAt = now
        )

        val user3 = User(
            id = UserId.of("3"),
            email = Email("saburo@example.com"),
            name = "三郎",
            createdAt = now,
            updatedAt = now
        )

        users[user1.id.value] = user1
        users[user2.id.value] = user2
        users[user3.id.value] = user3
    }

    override suspend fun findById(id: UserId): User? {
        return users[id.value]
    }

    override suspend fun findByEmail(email: String): User? {
        return users.values.find { it.email.value == email }
    }

    override suspend fun findAll(): List<User> {
        return users.values.toList().sortedBy { it.id.value }
    }

    override suspend fun save(user: User): User {
        users[user.id.value] = user
        return user
    }

    override suspend fun delete(id: UserId): Boolean {
        return users.remove(id.value) != null
    }

    override suspend fun existsByEmail(email: String): Boolean {
        return users.values.any { it.email.value == email }
    }
}