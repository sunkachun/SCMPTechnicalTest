package com.example.scmptechnicaltest.di

import com.example.data.login.AuthRepositoryImpl
import com.example.domain.login.repository.AuthRepository
import com.example.domain.login.usecase.Login

object ServiceLocator {
    private var authRepository: AuthRepository? = null
    private var loginUseCase: Login? = null

    fun provideAuthRepository(): AuthRepository {
        return authRepository ?: AuthRepositoryImpl().also { authRepository = it }
    }

    fun provideLoginUseCase(): Login {
        return loginUseCase ?: Login(provideAuthRepository()).also { loginUseCase = it }
    }
}