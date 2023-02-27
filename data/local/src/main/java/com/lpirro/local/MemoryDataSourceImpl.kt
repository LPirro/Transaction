package com.lpirro.local

import com.lpirro.local.model.Transaction
import java.util.LinkedList

class MemoryDataSourceImpl : MemoryDataSource {

    private val transactions: LinkedList<Transaction> = LinkedList()

    init {
        transactions.add(Transaction(emptyMap<String, String>().toMutableMap()))
    }

    override val pendingTransaction: Transaction
        get() = transactions.last

    override val size: Int
        get() = transactions.size

    override fun createTransaction(entries: MutableMap<String, String>) {
        transactions.add(Transaction(entries))
    }

    override fun getTransactions(): List<Transaction> = transactions

    override fun getTransactionAtIndex(index: Int) = try {
        transactions[index]
    } catch (e: IndexOutOfBoundsException) {
        null
    }

    override fun getValue(key: String): String? = pendingTransaction.entries[key]

    override fun setValue(key: String, value: String) {
        pendingTransaction.entries[key] = value
    }

    override fun remove(key: String) {
        pendingTransaction.entries.remove(key)
    }

    override fun removeTransaction() {
        transactions.removeLast()
    }
}
