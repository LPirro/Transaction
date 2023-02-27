package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class CommitUseCaseImpl(
    private val repository: TransactionRepository
) : CommitUseCase {

    override fun invoke(): Boolean {
        val transactions = repository.getTransactions()
        return if (transactions.size > 1) {
            repository.commit()
            true
        } else
            false
    }
}
