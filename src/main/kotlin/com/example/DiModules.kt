package com.example

import com.example.application.usecases.auth.GetCurrentUserUseCase
import com.example.application.usecases.auth.SyncUserFromClerkUseCase
import com.example.application.usecases.user.CreateUserUseCase
import com.example.application.usecases.user.DeleteUserUseCase
import com.example.application.usecases.user.GetUserUseCase
import com.example.application.usecases.user.ListUsersUseCase
import com.example.application.usecases.user.UpdateUserUseCase
import com.example.domain.repositories.UserRepository
import com.example.infrastructure.external.ClerkApiClient
import com.example.infrastructure.database.repositories.UserRepositoryImpl
import com.example.presentation.controllers.AuthController
import com.example.presentation.controllers.UserController
import org.koin.dsl.module

// Domain層のDIモジュール
val domainModule = module {}

// Application層のDIモジュール/
val applicationModule = module {
    single { CreateUserUseCase(get()) }
    single { GetUserUseCase(get()) }
    single { ListUsersUseCase(get()) }
    single { UpdateUserUseCase(get()) }
    single { DeleteUserUseCase(get()) }

    single { SyncUserFromClerkUseCase(get(), get()) }
    single { GetCurrentUserUseCase(get()) }
}

// Infrastructure層のDIモジュール
val infrastructureModule = module {
    single<UserRepository> { UserRepositoryImpl() }

    // External services
    single { ClerkApiClient() }
}

// Presentation層のDIモジュール
val presentationModule = module {
    single { UserController(get(), get(), get(), get(), get()) }
    single { AuthController(get(), get()) }
}