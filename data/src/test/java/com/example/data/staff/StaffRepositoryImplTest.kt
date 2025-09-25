package com.example.data.staff

import com.example.domain.staff.model.Staff
import com.example.domain.staff.model.StaffListResponse
import com.example.domain.staff.repository.StaffRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class StaffRepositoryImplTest {

    private val repository: StaffRepository = mockk()

    @Test
    fun `getStaff should return StaffListResponse when repository succeeds`() = runTest {
        val page = 1
        val expectedResponse = StaffListResponse(
            page = 1,
            perPage = 6,
            total = 12,
            totalPages = 2,
            data = listOf(
                Staff(1, "john.doe@example.com", "John", "Doe", "avatar_url_1"),
                Staff(2, "jane.doe@example.com", "Jane", "Doe", "avatar_url_2")
            )
        )

        coEvery { repository.getStaff(page) } returns expectedResponse

        val response = repository.getStaff(page)

        assertEquals(expectedResponse, response)
        coVerify(exactly = 1) { repository.getStaff(page) }
    }
}