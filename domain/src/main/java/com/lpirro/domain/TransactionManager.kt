package com.lpirro.domain

import com.lpirro.domain.model.Command
import kotlinx.coroutines.flow.Flow

interface TransactionManager {
    fun validateCommandProtection(input: String): Flow<Boolean>
    fun execute(input: String): Flow<Command>
}
