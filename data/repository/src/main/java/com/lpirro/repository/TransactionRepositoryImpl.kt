package com.lpirro.repository

import com.lpirro.domain.repository.TransactionRepository
import com.lpirro.local.MemoryDataSource

class TransactionRepositoryImpl(
    private val memoryDataSource: MemoryDataSource
) : TransactionRepository {

    override fun set(key: String, value: String) {
        memoryDataSource.setValue(key, value)
    }

    override fun get(key: String): String? {
        return memoryDataSource.getValue(key)
    }

    override fun remove(key: String) {
        memoryDataSource.remove(key)
    }

    override fun begin() {
        val currentTransaction = memoryDataSource.pendingTransaction.entries.toMutableMap()
        createTransaction(currentTransaction)
    }

    override fun commit() {
        getPendingTransaction().entries.forEach { (key, value) ->
            getTransactions().getOrNull(getTransactions().size - 2)?.entries?.set(key, value)
        }
        removePendingTransaction()
    }

    override fun getTransactions() = memoryDataSource.getTransactions()

    override fun getPendingTransaction() = memoryDataSource.pendingTransaction

    override fun getTransactionAtIndex(index: Int) = memoryDataSource.getTransactionAtIndex(index)

    override fun removePendingTransaction() {
        memoryDataSource.removeTransaction()
    }

    override fun createTransaction(transaction: MutableMap<String, String>) {
        memoryDataSource.createTransaction(transaction)
    }
}
