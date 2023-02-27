package com.lpirro.local

import com.lpirro.local.model.Transaction

interface MemoryDataSource {
    val pendingTransaction: Transaction
    val size: Int
    fun createTransaction(entries: MutableMap<String, String>)
    fun getTransactions(): List<Transaction>
    fun getTransactionAtIndex(index: Int): Transaction?
    fun getValue(key: String): String?
    fun setValue(key: String, value: String)
    fun remove(key: String)
    fun removeTransaction()
}
