package com.lpirro.repository

import com.lpirro.domain.repository.TransactionRepository
import com.lpirro.local.MemoryDataSource
import com.lpirro.local.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TransactionRepositoryTest {

    private lateinit var repository: TransactionRepository
    private val memoryDataSource: MemoryDataSource = mock()

    @Before
    fun setup() {
        repository = TransactionRepositoryImpl(memoryDataSource)
    }

    @Test
    fun `Set method transaction will call MemoryDataSource setValue`() {
        repository.set("name", "leonardo")
        verify(memoryDataSource, times(1)).setValue(any(), any())
    }

    @Test
    fun `Get method will get transaction from MemoryDataSource`() {
        whenever(memoryDataSource.getValue("name")).thenReturn("leonardo")
        val result = repository.get("name")
        verify(memoryDataSource, times(1)).getValue(any())
        assertEquals("leonardo", result)
    }

    @Test
    fun `Remove method will remove the transaction from MemoryDataSource`() {
        repository.remove("name")
        verify(memoryDataSource, times(1)).remove(any())
    }

    @Test
    fun `Remove pending transaction will remove the transaction from MemoryDataSource`() {
        repository.removePendingTransaction()
        verify(memoryDataSource, times(1)).removeTransaction()
    }

    @Test
    fun `Create transaction will create a new transaction on MemoryDataSource`() {
        repository.createTransaction(mutableMapOf("foo" to "123"))
        verify(memoryDataSource, times(1)).createTransaction(any())
    }

    @Test
    fun `Get pending transaction will return the pending transaction from MemoryDataSource`() {
        whenever(memoryDataSource.pendingTransaction).thenReturn(Transaction(mutableMapOf("foo" to "123")))
        val result = repository.getPendingTransaction()
        assertEquals("123", result.entries["foo"])
    }

    @Test
    fun `getTransactionAtIndex will return the corresponding transaction from MemoryDataSource`() {
        whenever(memoryDataSource.getTransactionAtIndex(any()))
            .thenReturn(Transaction(mutableMapOf("bar" to "1993")))
        val result = repository.getTransactionAtIndex(1)
        verify(memoryDataSource, times(1)).getTransactionAtIndex(1)
        assertEquals("1993", result!!.entries["bar"])
    }

    @Test
    fun `getTransactions will return all the transactions from MemoryDataSource`() {
        val mockedTransactions = listOf(
            Transaction(mutableMapOf("age" to "1993", "name" to "leonardo")),
            Transaction(mutableMapOf("age" to "1990", "name" to "wallet"))
        )

        whenever(memoryDataSource.getTransactions()).thenReturn(mockedTransactions)
        val result = repository.getTransactions()
        verify(memoryDataSource, times(1)).getTransactions()
        assertEquals(2, result.size)
    }
}
