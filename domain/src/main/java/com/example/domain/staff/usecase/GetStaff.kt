package com.example.domain.staff.usecase

import com.example.domain.staff.model.Staff
import com.example.domain.staff.repository.StaffRepository

class GetStaffUseCase(private val repository: StaffRepository) {
    suspend fun execute(page: Int, token: String): List<Staff> {
        return repository.getStaff(page, token)
    }
}