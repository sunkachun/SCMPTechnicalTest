package com.example.presentation.staff

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.staff.model.Staff
import com.example.domain.staff.model.StaffListResponse
import com.example.domain.staff.usecase.GetStaff
import com.example.presentation.staff.mapper.StaffMapper
import com.example.presentation.staff.model.StaffDisplayModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StaffViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getStaffUseCase: GetStaff
    private lateinit var mapper: StaffMapper
    private lateinit var viewModel: StaffViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getStaffUseCase = mockk()
        mapper = mockk()
        viewModel = StaffViewModel(getStaffUseCase, mapper)
    }

    @Test
    fun `loadInitial clears data and loads first page`() = runTest {
        // Given
        val staffListResponse = StaffListResponse(
            page = 1,
            perPage = 2,
            total = 4,
            totalPages = 2,
            data = listOf(
                Staff(1, "john.doe@example.com", "John", "Doe", "avatar1.jpg"),
                Staff(2, "jane.doe@example.com", "Jane", "Doe", "avatar2.jpg")
            )
        )

        val expectedDisplayModels = listOf(
            StaffDisplayModel("John Doe", "avatar1.jpg", "john.doe@example.com"),
            StaffDisplayModel("Jane Doe", "avatar2.jpg", "jane.doe@example.com")
        )

        coEvery { getStaffUseCase.execute(1) } returns staffListResponse
        every { mapper.mapToStaffDisplayModel(staffListResponse.data) } returns expectedDisplayModels

        // When
        viewModel.staffList.test {
            viewModel.loadInitial()

            // Then
            val initialList = awaitItem()
            assertEquals(emptyList<StaffDisplayModel>(), initialList) // Cleared list

            val finalList = awaitItem()
            assertEquals(expectedDisplayModels, finalList)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getStaffUseCase.execute(1) }
        coVerify { mapper.mapToStaffDisplayModel(staffListResponse.data) }
    }

    @Test
    fun `loadMore appends data to the list`() = runTest {
        // Given
        val initialResponse = StaffListResponse(
            page = 1,
            perPage = 2,
            total = 4,
            totalPages = 2,
            data = listOf(
                Staff(1, "john.doe@example.com", "John", "Doe", "avatar1.jpg"),
                Staff(2, "jane.doe@example.com", "Jane", "Doe", "avatar2.jpg")
            )
        )

        val secondResponse = StaffListResponse(
            page = 2,
            perPage = 2,
            total = 4,
            totalPages = 2,
            data = listOf(
                Staff(3, "jim.bean@example.com", "Jim", "Bean", "avatar3.jpg"),
                Staff(4, "jill.stone@example.com", "Jill", "Stone", "avatar4.jpg")
            )
        )

        val initialDisplayModels = listOf(
            StaffDisplayModel("John Doe", "avatar1.jpg", "john.doe@example.com"),
            StaffDisplayModel("Jane Doe", "avatar2.jpg", "jane.doe@example.com")
        )

        val secondDisplayModels = listOf(
            StaffDisplayModel("Jim Bean", "avatar3.jpg", "jim.bean@example.com"),
            StaffDisplayModel("Jill Stone", "avatar4.jpg", "jill.stone@example.com")
        )

        coEvery { getStaffUseCase.execute(1) } returns initialResponse
        coEvery { getStaffUseCase.execute(2) } returns secondResponse
        every { mapper.mapToStaffDisplayModel(initialResponse.data) } returns initialDisplayModels
        every { mapper.mapToStaffDisplayModel(secondResponse.data) } returns secondDisplayModels

        // When
        viewModel.staffList.test {
            viewModel.loadInitial()
            awaitItem()
            awaitItem()

            viewModel.loadMore()
            val updatedList = awaitItem()

            // Then
            assertEquals(initialDisplayModels + secondDisplayModels, updatedList)

            cancelAndIgnoreRemainingEvents()
        }

        coVerifySequence {
            getStaffUseCase.execute(1)
            mapper.mapToStaffDisplayModel(initialResponse.data)
            getStaffUseCase.execute(2)
            mapper.mapToStaffDisplayModel(secondResponse.data)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}