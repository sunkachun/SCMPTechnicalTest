package com.example.scmptechnicaltest.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.login.usecase.Login
import com.example.domain.staff.usecase.GetStaff
import com.example.presentation.login.LoginViewModel
import com.example.presentation.staff.StaffViewModel
import com.example.presentation.staff.mapper.StaffMapper
import com.example.scmptechnicaltest.di.ServiceLocator

class StaffViewModelFactory(
    private val staffUseCase: GetStaff,
    private val staffMapper: StaffMapper,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffViewModel::class.java)) {
            return StaffViewModel(staffUseCase, staffMapper) as T
        }
        throw IllegalArgumentException("${modelClass.name}")
    }

    companion object {
        fun create(): StaffViewModelFactory {
            return StaffViewModelFactory(
                ServiceLocator.provideGetStaffUseCase(),
                ServiceLocator.provideStaffMapper()
            )
        }
    }
}