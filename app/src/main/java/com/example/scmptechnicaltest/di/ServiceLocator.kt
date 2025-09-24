package com.example.scmptechnicaltest.di

import com.example.data.login.AuthRepositoryImpl
import com.example.data.staff.StaffRepositoryImpl
import com.example.domain.login.repository.AuthRepository
import com.example.domain.login.usecase.Login
import com.example.domain.staff.repository.StaffRepository
import com.example.domain.staff.usecase.GetStaff
import com.example.presentation.staff.mapper.StaffMapper

object ServiceLocator {
    private var authRepository: AuthRepository? = null
    private var staffRepository: StaffRepository? = null
    private var loginUseCase: Login? = null
    private var staffUseCase: GetStaff? = null

    fun provideAuthRepository(): AuthRepository {
        return authRepository ?: AuthRepositoryImpl().also { authRepository = it }
    }

    fun provideLoginUseCase(): Login {
        return loginUseCase ?: Login(provideAuthRepository()).also { loginUseCase = it }
    }

    fun registerAuthRepository(customRepository: AuthRepository) {
        authRepository = customRepository
    }

    fun registerLoginUseCase(customUseCase: Login) {
        loginUseCase = customUseCase
    }

    fun provideStaffRepository(): StaffRepository {
        return staffRepository ?: StaffRepositoryImpl().also { staffRepository = it }
    }

    fun provideGetStaffUseCase(): GetStaff {
        return staffUseCase ?: GetStaff(provideStaffRepository()).also { staffUseCase = it }
    }

    fun provideStaffMapper(): StaffMapper {
        return StaffMapper()
    }

    fun registerStaffRepository(customRepository: StaffRepository) {
        staffRepository = customRepository
    }

    fun registerGetStaffUseCase(customUseCase: GetStaff) {
        staffUseCase = customUseCase
    }
}