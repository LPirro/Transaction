package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class GetUseCaseImpl(
    private val repository: TransactionRepository
) : GetUseCase {
    override fun invoke(key: String): String? {
        return repository.get(key)
    }
}
