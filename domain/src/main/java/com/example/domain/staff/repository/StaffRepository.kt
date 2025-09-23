package com.example.domain.staff.repository

import com.example.domain.staff.model.StaffListResponse

interface StaffRepository {

    suspend fun getStaff(page: Int): StaffListResponse
}