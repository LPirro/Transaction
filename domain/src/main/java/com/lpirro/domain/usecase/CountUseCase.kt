package com.lpirro.domain.usecase

interface CountUseCase {
    operator fun invoke(value: String): Int
}
