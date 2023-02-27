package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class BeginUseCaseImpl(
    private val repository: TransactionRepository
) : BeginUseCase {
    override fun invoke() {
        repository.begin()
    }
}
