package com.example.domain.login.model

data class LoginResponse(val token: String, val error: String? = null)