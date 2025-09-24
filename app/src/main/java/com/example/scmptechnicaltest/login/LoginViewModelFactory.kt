package com.example.scmptechnicaltest.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.login.usecase.Login
import com.example.presentation.login.LoginViewModel
import com.example.scmptechnicaltest.di.ServiceLocator

class LoginViewModelFactory(private val loginUseCase: Login) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase) as T
        }
        throw IllegalArgumentException("${modelClass.name}")
    }

    companion object {
        fun create(): LoginViewModelFactory {
            return LoginViewModelFactory(ServiceLocator.provideLoginUseCase())
        }
    }
}