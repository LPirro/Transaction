package com.lpirro.domain.parser

import com.lpirro.domain.model.CommandType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class CommandParserTest {

    private lateinit var commandParser: CommandParser

    @Before
    fun setup() {
        commandParser = CommandParserImpl()
    }

    @Test
    fun `Command with two arguments parsed successfully`() {
        val commandUnderTest = "SET foo 123"
        val result = commandParser.parseInputCommand(commandUnderTest)

        assertEquals("SET", CommandType.SET.value)
        assertEquals("foo", result.key)
        assertEquals("123", result.value)
    }

    @Test
    fun `Command with one argument parsed successfully`() {
        val commandUnderTest = "DELETE foo"
        val result = commandParser.parseInputCommand(commandUnderTest)

        assertEquals("DELETE", CommandType.DELETE.value)
        assertEquals("foo", result.key)
        assertEquals(null, result.value)
    }

    @Test
    fun `Command with no arguments parsed successfully`() {
        val commandUnderTest = "COMMIT"
        val result = commandParser.parseInputCommand(commandUnderTest)

        assertEquals("COMMIT", CommandType.COMMIT.value)
        assertEquals(null, result.key)
        assertEquals(null, result.value)
    }

    @Test
    fun `Command with whitespaces parsed successfully`() {
        val commandUnderTest = "SET      foo  123"
        val result = commandParser.parseInputCommand(commandUnderTest)

        assertEquals("SET", CommandType.SET.value)
        assertEquals("foo", result.key)
        assertEquals("123", result.value)
    }

    @Test
    fun `Blank command will throw IllegalStateException`() {
        val commandUnderTest = ""
        assertFailsWith(
            exceptionClass = IllegalStateException::class,
            message = "No exception found",
            block = { commandParser.parseInputCommand(commandUnderTest) }
        )
    }
}
