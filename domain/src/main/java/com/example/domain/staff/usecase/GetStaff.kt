package com.example.domain.staff.usecase

import com.example.domain.staff.model.Staff
import com.example.domain.staff.model.StaffListResponse
import com.example.domain.staff.repository.StaffRepository

class GetStaff(private val repository: StaffRepository) {
    suspend fun execute(page: Int): StaffListResponse {
        return repository.getStaff(page)
    }
}