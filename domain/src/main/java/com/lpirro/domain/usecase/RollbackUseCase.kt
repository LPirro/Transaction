package com.lpirro.domain.usecase

interface RollbackUseCase {
    operator fun invoke(): Boolean
}
