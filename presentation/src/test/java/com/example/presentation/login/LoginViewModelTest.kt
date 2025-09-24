package com.example.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.login.model.LoginRequest
import com.example.domain.login.model.LoginResponse
import com.example.domain.login.usecase.Login
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var loginUseCase: Login
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `login with invalid email emits error state`() = runTest {
        val invalidEmail = "invalid-email"
        val password = "password123"

        viewModel.login(invalidEmail, password)

        val state = viewModel.uiState.first()
        assertEquals(LoginUiState.Error("Please enter a valid email address"), state)
    }

    @Test
    fun `login with invalid password emits error state`() = runTest {
        val email = "test@example.com"
        val invalidPassword = "123"

        viewModel.login(email, invalidPassword)

        val state = viewModel.uiState.first()
        assertEquals(
            LoginUiState.Error("Password must be at least 6 characters"),
            state
        )
    }

    @Test
    fun `login with valid credentials emits success state`() = runTest {
        val email = "eve.holt@reqres.in"
        val password = "cityslicka"
        val token = "QpwL5tke4Pnpja7X4"

        val loginRequest = LoginRequest(email, password)
        coEvery { loginUseCase.execute(loginRequest) } returns
                LoginResponse(token, null)

        viewModel.uiState.test {
            viewModel.login(email, password)

            awaitItem()
            awaitItem()

            val successState = awaitItem()
            assertEquals(LoginUiState.Success(token), successState)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { loginUseCase.execute(loginRequest) }
    }

    @Test
    fun `login with server error emits error state`() = runTest {
        val email = "test@example.com"
        val password = "password"

        val loginRequest = LoginRequest(email, password)
        coEvery { loginUseCase.execute(loginRequest) } returns
                LoginResponse("", "Invalid credentials")

        viewModel.uiState.test {
            viewModel.login(email, password)

            awaitItem()
            awaitItem()

            val errorState = awaitItem()
            assertEquals(LoginUiState.Error("Login failed: Invalid credentials"), errorState)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { loginUseCase.execute(loginRequest) }
    }

    @Test
    fun `login with exception emits error state`() = runTest {
        val email = "test@example.com"
        val password = "password"

        val loginRequest = LoginRequest(email, password)
        coEvery { loginUseCase.execute(loginRequest) } throws RuntimeException("Network error")

        viewModel.uiState.test {
            viewModel.login(email, password)

            awaitItem()
            awaitItem()

            val errorState = awaitItem()
            assertEquals(LoginUiState.Error("Login failed: Network error"), errorState)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { loginUseCase.execute(loginRequest) }
    }

    @Test
    fun `reset state sets uiState to Idle`() = runTest {
        viewModel.resetState()

        val state = viewModel.uiState.value
        assertEquals(LoginUiState.Idle, state)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}