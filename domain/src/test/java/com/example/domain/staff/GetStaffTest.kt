package com.example.domain.staff.usecase

import com.example.domain.staff.model.Staff
import com.example.domain.staff.model.StaffListResponse
import com.example.domain.staff.repository.StaffRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetStaffTest {

    private val repository: StaffRepository = mockk()

    private val getStaff = GetStaff(repository)

    @Test
    fun `execute should return StaffListResponse when repository getStaff succeeds`() = runTest {
        val page = 1
        val expectedResponse = StaffListResponse(
            page = 1,
            perPage = 6,
            total = 12,
            totalPages = 2,
            data = listOf(
                Staff(
                    id = 1,
                    email = "george.bluth@reqres.in",
                    firstName = "George",
                    lastName = "Bluth",
                    avatar = "https://reqres.in/img/faces/1-image.jpg"
                ),
                Staff(
                    id = 2,
                    email = "janet.weaver@reqres.in",
                    firstName = "Janet",
                    lastName = "Weaver",
                    avatar = "https://reqres.in/img/faces/2-image.jpg"
                )
            )
        )

        coEvery { repository.getStaff(page) } returns expectedResponse

        val response = getStaff.execute(page)

        assertEquals(expectedResponse, response)
        coVerify(exactly = 1) { repository.getStaff(page) }
    }

    @Test
    fun `execute should return empty staff list when page has no data`() = runTest {
        val page = 99
        val expectedResponse = StaffListResponse(
            page = 99,
            perPage = 6,
            total = 0,
            totalPages = 0,
            data = emptyList()
        )

        coEvery { repository.getStaff(page) } returns expectedResponse

        val response = getStaff.execute(page)

        assertEquals(expectedResponse, response)
        assertEquals(0, response.data.size)
        coVerify(exactly = 1) { repository.getStaff(page) }
    }

    @Test
    fun `execute should handle different page numbers correctly`() = runTest {
        val page = 2
        val expectedResponse = StaffListResponse(
            page = 2,
            perPage = 6,
            total = 12,
            totalPages = 2,
            data = listOf(
                Staff(
                    id = 7,
                    email = "michael.lawson@reqres.in",
                    firstName = "Michael",
                    lastName = "Lawson",
                    avatar = "https://reqres.in/img/faces/7-image.jpg"
                )
            )
        )

        coEvery { repository.getStaff(page) } returns expectedResponse

        val response = getStaff.execute(page)

        assertEquals(expectedResponse, response)
        assertEquals(page, response.page)
        coVerify(exactly = 1) { repository.getStaff(page) }
    }

    @Test
    fun `execute should verify correct page parameter is passed to repository`() = runTest {
        val page = 5
        val expectedResponse = StaffListResponse(
            page = 5,
            perPage = 6,
            total = 0,
            totalPages = 0,
            data = emptyList()
        )

        coEvery { repository.getStaff(page) } returns expectedResponse

        val response = getStaff.execute(page)

        assertEquals(page, response.page)

        coVerify(exactly = 1) { repository.getStaff(page) }
    }
}