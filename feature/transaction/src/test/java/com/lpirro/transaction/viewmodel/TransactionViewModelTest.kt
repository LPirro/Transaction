package com.lpirro.transaction.viewmodel

import app.cash.turbine.test
import com.lpirro.domain.TransactionManager
import com.lpirro.domain.model.Command
import com.lpirro.domain.model.CommandType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class TransactionViewModelTest {

    private lateinit var viewModel: TransactionViewModel
    private var transactionManager: TransactionManager = mock()

    @Before
    fun setup() {
        viewModel = TransactionViewModel(transactionManager)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `perform SET will execute the command correctly`() = runTest {
        val mockedResult = Command.Input(CommandType.SET, "FOO", "123")
        whenever(transactionManager.execute("SET FOO 123")).thenReturn(flow { emit(mockedResult) })

        viewModel.executeTransaction("SET FOO 123")

        viewModel.uiState.test {
            skipItems(1)
            val result = awaitItem().commandList[0].toString()
            assertEquals("SET FOO 123", result)
        }
    }

    @Test
    fun `perform GET will execute the command correctly`() = runTest {
        val mockedResult = Command.Output("123")
        whenever(transactionManager.execute("GET FOO")).thenReturn(flow { emit(mockedResult) })

        viewModel.executeTransaction("GET FOO")

        viewModel.uiState.test {
            skipItems(1)
            val result = awaitItem().commandList[0] as Command.Output
            assertEquals("123", result.output)
        }
    }

    @Test
    fun `Validate Command protection will emit correct validation`() = runTest {
        whenever(transactionManager.validateCommandProtection("DELETE FOO")).thenReturn(flow { emit(true) })

        viewModel.validateCommandProtection("DELETE FOO")

        viewModel.uiState.test {
            skipItems(1)
            val result = awaitItem()
            assertEquals(true, result.commandProtectionDialog)
        }
    }
}
