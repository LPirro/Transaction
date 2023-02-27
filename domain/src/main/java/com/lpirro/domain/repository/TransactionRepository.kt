package com.lpirro.domain.repository

import com.lpirro.local.model.Transaction

interface TransactionRepository {
    fun set(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String)
    fun begin()
    fun commit()
    fun getTransactions(): List<Transaction>
    fun getPendingTransaction(): Transaction
    fun getTransactionAtIndex(index: Int): Transaction?
    fun removePendingTransaction()
    fun createTransaction(transaction: MutableMap<String, String>)
}
