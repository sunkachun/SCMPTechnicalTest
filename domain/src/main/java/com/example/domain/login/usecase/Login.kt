package com.example.domain.login.usecase

import com.example.domain.login.model.LoginRequest
import com.example.domain.login.model.LoginResponse
import com.example.domain.login.repository.AuthRepository

class Login(private val repository: AuthRepository) {
    suspend fun execute(request: LoginRequest): LoginResponse {
        return repository.login(request)
    }
}