package com.lpirro.transaction.viewmodel

import kotlinx.coroutines.Job

interface TransactionViewModelContract {
    fun executeTransaction(input: String): Job
    fun validateCommandProtection(input: String): Job
}
