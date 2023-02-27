package com.lpirro.local

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MemoryDataSourceTest {

    private lateinit var memoryDataSource: MemoryDataSource

    @Before
    fun setup() {
        memoryDataSource = MemoryDataSourceImpl()
    }

    @Test
    fun `Set value will update pending transaction`() {
        memoryDataSource.setValue("foo", "awesome")
        val result = memoryDataSource.pendingTransaction.entries["foo"]
        assertEquals("awesome", result)
    }

    @Test
    fun `Get value will return correct transaction`() {
        memoryDataSource.setValue("foo", "awesome")
        memoryDataSource.setValue("leo", "pirro")
        memoryDataSource.setValue("age", "16")

        assertEquals("awesome", memoryDataSource.getValue("foo"))
        assertEquals("pirro", memoryDataSource.getValue("leo"))
        assertEquals("16", memoryDataSource.getValue("age"))
    }

    @Test
    fun `Delete value will remove the current transaction`() {
        memoryDataSource.setValue("foo", "awesome")
        memoryDataSource.setValue("age", "16")
        memoryDataSource.remove("age")

        assertEquals("awesome", memoryDataSource.getValue("foo"))
        assertEquals(null, memoryDataSource.getValue("age"))
    }

    @Test
    fun `Create transaction will update transactions list`() {
        val entries = mutableMapOf(
            "Tony Stark" to "Ironman",
            "Bruce Wayne" to "Batman",
            "Bruce Banner" to "Hulk"
        )
        memoryDataSource.createTransaction(entries)

        val transaction = memoryDataSource.getTransactionAtIndex(1)!!
        assertEquals("Ironman", transaction.entries["Tony Stark"])
        assertEquals("Batman", transaction.entries["Bruce Wayne"])
        assertEquals("Hulk", transaction.entries["Bruce Banner"])
        assertEquals(2, memoryDataSource.size)
    }

    @Test
    fun `Delete transaction will remove transaction from the list`() {
        val entries = mutableMapOf(
            "Tony Stark" to "Ironman",
            "Bruce Wayne" to "Batman",
            "Bruce Banner" to "Hulk"
        )
        memoryDataSource.createTransaction(entries)
        assertEquals(2, memoryDataSource.size)
        memoryDataSource.removeTransaction()
        assertEquals(1, memoryDataSource.size)
    }
}
