package com.lpirro.repository.di

import com.lpirro.domain.repository.TransactionRepository
import com.lpirro.local.MemoryDataSource
import com.lpirro.repository.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTransactionRepository(memoryDataSource: MemoryDataSource): TransactionRepository {
        return TransactionRepositoryImpl(memoryDataSource = memoryDataSource)
    }
}
