package com.example.domain.login.repository

import com.example.domain.login.model.LoginRequest
import com.example.domain.login.model.LoginResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): LoginResponse
}