package com.lpirro.local.di

import com.lpirro.local.MemoryDataSource
import com.lpirro.local.MemoryDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemoryDataSourceModule {
    @Provides
    @Singleton
    fun provideMemoryDataSource(): MemoryDataSource = MemoryDataSourceImpl()
}
