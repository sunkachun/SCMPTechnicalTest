package com.example.presentation.staff.mapper

import com.example.domain.staff.model.Staff
import com.example.presentation.staff.model.StaffDisplayModel

class StaffMapper {
    fun mapToStaffDisplayModel(response: List<Staff>): List<StaffDisplayModel> {
        return response.map { staff ->
            StaffDisplayModel(
                name = "${staff.firstName} ${staff.lastName}",
                avatar = staff.avatar,
                email = staff.email,
            )
        }
    }
}