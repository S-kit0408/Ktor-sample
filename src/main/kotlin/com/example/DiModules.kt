package com.example

import com.example.application.usecases.*
import com.example.domain.repositories.UserRepository
import com.example.domain.services.UserDomainService
import com.example.infrastructure.database.repositories.UserRepositoryImpl
import com.example.infrastructure.database.repositories.InMemoryUserRepository
import com.example.presentation.controllers.UserController
import org.koin.dsl.module

// Domain層のDIモジュール
val domainModule = module {
    single { UserDomainService(get()) }
}

// Application層のDIモジュール/
val applicationModule = module {
    single { CreateUserUseCase(get(), get()) }
    single { GetUserUseCase(get()) }
    single { ListUsersUseCase(get()) }
    single { UpdateUserUseCase(get(), get()) }
    single { DeleteUserUseCase(get()) }
}

// Infrastructure層のDIモジュール
val infrastructureModule = module {
    single<UserRepository> { UserRepositoryImpl() }

    single<UserRepository> { InMemoryUserRepository() }
}

// Presentation層のDIモジュール
val presentationModule = module {
    single { UserController(get(), get(), get(), get(), get()) }
}