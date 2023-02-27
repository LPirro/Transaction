package com.lpirro.domain.usecase

interface GetUseCase {
    operator fun invoke(key: String): String?
}
