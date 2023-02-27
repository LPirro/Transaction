package com.lpirro.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lpirro.domain.TransactionManager
import com.lpirro.domain.model.Command
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionManager: TransactionManager
) : ViewModel(), TransactionViewModelContract {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    override fun executeTransaction(input: String): Job {
        return transactionManager.execute(input)
            .onEach { command ->
                val list = _uiState.value.commandList
                list.add(command)
                _uiState.value = TransactionUiState(commandList = list, commandInput = "")
            }
            .launchIn(viewModelScope)
    }

    /**
     * This method validates the command before execution
     * As per requirement some command are protected and requires a confirmation by the user
     *
     * If the command is unprotected it will call [executeTransaction] directly otherwise
     * it will emit a TransactionUiState to display a dialog to the user.
     *
     * @param input the input command to validate
     *
     * Protected commands are COMMIT, DELETE, ROLLBACK. See [com.lpirro.domain.model.CommandType.protected]
     */
    override fun validateCommandProtection(input: String): Job {
        return transactionManager.validateCommandProtection(input)
            .onEach { protected ->
                if (protected) _uiState.update {
                    it.copy(
                        commandProtectionDialog = true,
                        id = UUID.randomUUID().toString(),
                        commandInput = input
                    )
                } else executeTransaction(input)
            }.launchIn(viewModelScope)
    }

    data class TransactionUiState(
        var commandList: ArrayList<Command> = ArrayList(),
        val commandProtectionDialog: Boolean = false,
        var commandInput: String = "",
        val id: String = UUID.randomUUID().toString()
    )
}
