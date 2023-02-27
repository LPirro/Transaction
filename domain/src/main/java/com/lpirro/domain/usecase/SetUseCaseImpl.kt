package com.lpirro.domain.usecase

import com.lpirro.domain.repository.TransactionRepository

class SetUseCaseImpl(
    val repository: TransactionRepository
) : SetUseCase {
    override fun invoke(key: String, value: String) {
        repository.set(key, value)
    }
}
