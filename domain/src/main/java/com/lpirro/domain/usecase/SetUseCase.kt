package com.lpirro.domain.usecase

interface SetUseCase {
    operator fun invoke(key: String, value: String)
}
