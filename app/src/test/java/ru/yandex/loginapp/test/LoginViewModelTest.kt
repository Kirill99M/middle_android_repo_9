package ru.yandex.loginapp.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.yandex.loginapp.LoginScreenState.EmailValidationError
import ru.yandex.loginapp.LoginScreenState.EmptyFieldsError
import ru.yandex.loginapp.LoginScreenState.Loading
import ru.yandex.loginapp.LoginScreenState.Success
import ru.yandex.loginapp.LoginViewModel

private const val VALID_EMAIL = "Test@mail.ru"
private const val VALID_PASSWORD = "testPassword"

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        viewModel.login("", "")
        assert(viewModel.state.value is EmptyFieldsError)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        viewModel.login("testError", VALID_PASSWORD)
        assert(viewModel.state.value is EmailValidationError)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        viewModel.login(VALID_EMAIL, VALID_PASSWORD)
        testDispatcher.scheduler.runCurrent()
        assert(viewModel.state.value is Loading)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        viewModel.login(VALID_EMAIL, VALID_PASSWORD)
        testDispatcher.scheduler.runCurrent()
        testDispatcher.scheduler.advanceTimeBy(3100)
        assert(viewModel.state.value is Success)
    }

}