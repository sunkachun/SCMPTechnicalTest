package com.example.domain.staff.repository

import com.example.domain.staff.model.Staff

interface StaffRepository {

    suspend fun getStaff(page: Int, token: String): List<Staff>
}