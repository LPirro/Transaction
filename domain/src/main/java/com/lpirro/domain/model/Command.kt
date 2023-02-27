package com.lpirro.domain.model

sealed class Command {
    data class Input(val type: CommandType, val key: String?, val value: String?) : Command() {
        override fun toString(): String {
            return buildString {
                append(type.name)
                key?.let { append(" $it") }
                value?.let { append(" $it") }
            }
        }
    }
    data class Output(val output: String) : Command()
    data class Error(val message: String) : Command()
}
