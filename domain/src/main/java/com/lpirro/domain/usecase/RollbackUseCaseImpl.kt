package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class RollbackUseCaseImpl(
    private val repository: TransactionRepository
) : RollbackUseCase {
    override fun invoke(): Boolean {
        val transactions = repository.getTransactions()
        return if (transactions.size > 1) {
            repository.removePendingTransaction()
            true
        } else {
            false
        }
    }
}
