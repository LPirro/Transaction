package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class DeleteUseCaseImpl(
    private val repository: TransactionRepository
) : DeleteUseCase {
    override fun invoke(key: String) {
        repository.remove(key)
    }
}
