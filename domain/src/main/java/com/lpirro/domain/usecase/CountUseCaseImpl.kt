package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class CountUseCaseImpl(
    private val repository: TransactionRepository
) : CountUseCase {
    override fun invoke(value: String): Int {
        return repository.getPendingTransaction().entries.count { it.value == value }
    }
}
