package com.example.data.login

import com.example.domain.login.model.LoginRequest
import com.example.domain.login.model.LoginResponse
import com.example.domain.login.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthRepositoryImplTest {

    private val repository: AuthRepository = mockk()

    @Test
    fun `login should return LoginResponse when repository login succeeds`() = runTest {
        val request = LoginRequest("username", "password")
        val expectedResponse = LoginResponse(token = "fake_token", error = null)

        coEvery { repository.login(request) } returns expectedResponse

        val response = repository.login(request)

        assertEquals(expectedResponse, response)
        coVerify(exactly = 1) { repository.login(request) }
    }
}