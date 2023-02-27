package com.lpirro.domain.di

import com.lpirro.domain.TransactionManager
import com.lpirro.domain.TransactionManagerImpl
import com.lpirro.domain.parser.CommandParser
import com.lpirro.domain.parser.CommandParserImpl
import com.lpirro.domain.repository.TransactionRepository
import com.lpirro.domain.usecase.BeginUseCase
import com.lpirro.domain.usecase.BeginUseCaseImpl
import com.lpirro.domain.usecase.CommitUseCase
import com.lpirro.domain.usecase.CommitUseCaseImpl
import com.lpirro.domain.usecase.CountUseCase
import com.lpirro.domain.usecase.CountUseCaseImpl
import com.lpirro.domain.usecase.DeleteUseCase
import com.lpirro.domain.usecase.DeleteUseCaseImpl
import com.lpirro.domain.usecase.GetUseCase
import com.lpirro.domain.usecase.GetUseCaseImpl
import com.lpirro.domain.usecase.RollbackUseCase
import com.lpirro.domain.usecase.RollbackUseCaseImpl
import com.lpirro.domain.usecase.SetUseCase
import com.lpirro.domain.usecase.SetUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideCommandParser(): CommandParser = CommandParserImpl()

    @Provides
    @Singleton
    fun provideTransactionManager(
        commandParser: CommandParser,
        setUseCase: SetUseCase,
        getUseCase: GetUseCase,
        deleteUseCase: DeleteUseCase,
        countUseCase: CountUseCase,
        beginUseCase: BeginUseCase,
        rollbackUseCase: RollbackUseCase,
        commitUseCase: CommitUseCase
    ): TransactionManager {
        return TransactionManagerImpl(
            commandParser = commandParser,
            setUseCase = setUseCase,
            getUseCase = getUseCase,
            deleteUseCase = deleteUseCase,
            countUseCase = countUseCase,
            beginUseCase = beginUseCase,
            rollbackUseCase = rollbackUseCase,
            commitUseCase = commitUseCase
        )
    }

    @Provides
    fun provideSetUseCase(repository: TransactionRepository): SetUseCase {
        return SetUseCaseImpl(repository)
    }

    @Provides
    fun provideGetUseCase(repository: TransactionRepository): GetUseCase {
        return GetUseCaseImpl(repository)
    }

    @Provides
    fun provideDeleteUseCase(repository: TransactionRepository): DeleteUseCase {
        return DeleteUseCaseImpl(repository)
    }

    @Provides
    fun provideCountUseCase(repository: TransactionRepository): CountUseCase {
        return CountUseCaseImpl(repository)
    }

    @Provides
    fun provideBeginUseCase(repository: TransactionRepository): BeginUseCase {
        return BeginUseCaseImpl(repository)
    }

    @Provides
    fun provideRollbackUseCase(repository: TransactionRepository): RollbackUseCase {
        return RollbackUseCaseImpl(repository)
    }

    @Provides
    fun provideCommitUseCase(repository: TransactionRepository): CommitUseCase {
        return CommitUseCaseImpl(repository)
    }
}
