package com.lpirro.domain

import com.lpirro.domain.model.Command
import com.lpirro.domain.model.CommandType
import com.lpirro.domain.parser.CommandParser
import com.lpirro.domain.usecase.BeginUseCase
import com.lpirro.domain.usecase.CommitUseCase
import com.lpirro.domain.usecase.CountUseCase
import com.lpirro.domain.usecase.DeleteUseCase
import com.lpirro.domain.usecase.GetUseCase
import com.lpirro.domain.usecase.RollbackUseCase
import com.lpirro.domain.usecase.SetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class TransactionManagerImpl(
    private val commandParser: CommandParser,
    private val setUseCase: SetUseCase,
    private val getUseCase: GetUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val countUseCase: CountUseCase,
    private val beginUseCase: BeginUseCase,
    private val rollbackUseCase: RollbackUseCase,
    private val commitUseCase: CommitUseCase
) : TransactionManager {

    override fun validateCommandProtection(input: String) = flow {
        val command = commandParser.parseInputCommand(input)
        emit(command.type.protected)
    }.flowOn(Dispatchers.Default)

    override fun execute(input: String): Flow<Command> {
        val command = commandParser.parseInputCommand(input)
        return transaction(command)
            .onStart {
                if (command.type != CommandType.UNKNOWN) emit(command)
            }
            .flowOn(Dispatchers.Default)
            .catch { emit(Command.Error("Something went wrong. Please try again.")) }
    }

    private fun transaction(input: Command.Input) = flow {
        when (input.type) {
            CommandType.SET -> performSet(input)
            CommandType.GET -> performGet(input)
            CommandType.DELETE -> performDelete(input)
            CommandType.COUNT -> performCount(input)
            CommandType.BEGIN -> performBegin(input)
            CommandType.COMMIT -> performCommit(input)
            CommandType.ROLLBACK -> performRollback(input)
            CommandType.UNKNOWN -> emit(Command.Error(message = "Unsupported Command"))
        }
    }

    private suspend fun FlowCollector<Command>.performSet(input: Command.Input) {
        if (input.key == null || input.value == null) {
            emit(Command.Error("error: please specify a key and a value (usage: SET <key> <value>)"))
        } else {
            setUseCase(input.key, input.value)
        }
    }

    private suspend fun FlowCollector<Command>.performGet(input: Command.Input) {
        if (input.key == null) {
            emit(Command.Error("error: please specify a key (usage: GET <key>)"))
        } else {
            getUseCase(input.key)?.let { emit(Command.Output(it)) }
                ?: emit(Command.Error("key ${input.key} not set"))
        }
    }

    private suspend fun FlowCollector<Command>.performDelete(input: Command.Input) {
        if (input.key == null) {
            emit(Command.Error("error: please specify a key (usage: DELETE <key>)"))
        } else {
            deleteUseCase(input.key)
        }
    }

    private suspend fun FlowCollector<Command>.performCount(input: Command.Input) {
        if (input.key == null) {
            emit(Command.Error("error: please specify a value (usage: COUNT <value>)"))
        } else {
            emit(Command.Output(countUseCase(input.key).toString()))
        }
    }

    private suspend fun FlowCollector<Command>.performBegin(input: Command.Input) {
        if (input.key != null || input.value != null) {
            emit(Command.Error("error: BEGIN does not support key value (usage: BEGIN)"))
        } else {
            beginUseCase.invoke()
        }
    }

    private suspend fun FlowCollector<Command>.performRollback(input: Command.Input) {
        if (input.key != null || input.value != null) {
            emit(Command.Error("error: ROLLBACK does not support key value (usage: ROLLBACK)"))
        } else {
            val success = rollbackUseCase()
            if (!success) emit(Command.Error("No transaction (Use BEGIN to start a new transaction)"))
        }
    }

    private suspend fun FlowCollector<Command>.performCommit(input: Command.Input) {
        if (input.key != null || input.value != null) {
            emit(Command.Error("error: COMMIT does not support key value (usage: COMMIT)"))
        } else {
            val success = commitUseCase()
            if (!success) emit(Command.Error("No transaction (Use BEGIN to start a new transaction)"))
        }
    }
}
