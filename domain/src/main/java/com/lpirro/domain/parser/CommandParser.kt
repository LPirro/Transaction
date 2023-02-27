package com.lpirro.domain.parser

import com.lpirro.domain.model.Command
import com.lpirro.domain.model.CommandType

interface CommandParser {
    fun parseInputCommand(commandInput: String): Command.Input
    fun mapCommandInput(type: String): CommandType
}
