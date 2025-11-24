package com.example

import com.example.application.usecases.user.CreateUserUseCase
import com.example.application.usecases.user.DeleteUserUseCase
import com.example.application.usecases.user.GetUserUseCase
import com.example.application.usecases.user.ListUsersUseCase
import com.example.application.usecases.user.UpdateUserUseCase
import com.example.domain.repositories.UserRepository
import com.example.domain.services.UserDomainService
import com.example.infrastructure.database.repositories.UserRepositoryImpl
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
}

// Presentation層のDIモジュール
val presentationModule = module {
    single { UserController(get(), get(), get(), get(), get()) }
}