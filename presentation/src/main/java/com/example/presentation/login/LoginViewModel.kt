package com.example.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.login.model.LoginRequest
import com.example.domain.login.usecase.Login
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: Login) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        // Email validation
        if (!isValidEmail(email)) {
            _uiState.value = LoginUiState.Error("Please enter a valid email address")
            return
        }

        // Password validation
        val passwordValidation = validatePassword(password)
        if (!passwordValidation.isValid) {
            _uiState.value = LoginUiState.Error(passwordValidation.errorMessage ?: "Invalid password")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = loginUseCase.execute(request)
                _uiState.value = LoginUiState.Success(response.token)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Login failed: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false

        // 簡單的郵箱驗證規則
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }

    private fun validatePassword(password: String): PasswordValidation {
        return when {
            password.length < 6 -> PasswordValidation(false, "Password must be at least 6 characters")
            password.length > 10 -> PasswordValidation(false, "Password must be at most 10 characters")
            !password.matches("^[a-zA-Z0-9]*$".toRegex()) -> PasswordValidation(false, "Password can only contain letters and numbers")
            else -> PasswordValidation(true)
        }
    }

    private data class PasswordValidation(val isValid: Boolean, val errorMessage: String? = null)
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}