package com.lpirro.domain.parser

import com.lpirro.domain.model.Command
import com.lpirro.domain.model.CommandType
import java.util.regex.Pattern

const val COMMAND_GROUP_REGEX = "^(\\S+)(?:\\s+(\\S+))?(?:\\s+(\\S+))?(?:\\s+(\\S+))?"

class CommandParserImpl : CommandParser {
    override fun parseInputCommand(commandInput: String): Command.Input {
        val pattern = Pattern.compile(COMMAND_GROUP_REGEX)
        val matcher = pattern.matcher(commandInput.uppercase())
        matcher.find()

        return try {
            Command.Input(
                type = mapCommandInput(matcher.group(1)!!),
                key = matcher.group(2),
                value = matcher.group(3)
            )
        } catch (e: java.lang.IllegalStateException) {
            Command.Input(CommandType.UNKNOWN, null, null)
        }
    }

    override fun mapCommandInput(type: String): CommandType {
        return when (type.uppercase()) {
            "SET" -> CommandType.SET
            "GET" -> CommandType.GET
            "DELETE" -> CommandType.DELETE
            "COUNT" -> CommandType.COUNT
            "BEGIN" -> CommandType.BEGIN
            "COMMIT" -> CommandType.COMMIT
            "ROLLBACK" -> CommandType.ROLLBACK
            else -> CommandType.UNKNOWN
        }
    }
}
