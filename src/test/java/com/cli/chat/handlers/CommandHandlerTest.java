package com.cli.chat.handlers;

import com.cli.chat.models.enums.Command;
import com.cli.chat.models.records.ParsedCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    @BeforeEach
    void setUp() {
        StateHandler.init(); // Ensure state is initialized before each test
    }

    @Test
    void handleCommand_ShouldExecuteValidCommand() {
        assertDoesNotThrow(() -> CommandHandler.handleCommand("chats"));
    }

    @Test
    void handleCommand_ShouldHandleInvalidCommand() {
        assertDoesNotThrow(() -> CommandHandler.handleCommand("invalidCommand"));
    }

    @Test
    void parseCommand_ShouldReturnValidParsedCommand() {
        Optional<ParsedCommand> parsedCommand = CommandHandler.parseCommand("chats");
        assertTrue(parsedCommand.isPresent());
        assertEquals(Command.CHATS, parsedCommand.get().command());
    }

    @Test
    void parseCommand_ShouldReturnEmptyForInvalidCommand() {
        Optional<ParsedCommand> parsedCommand = CommandHandler.parseCommand("invalidCommand");
        assertTrue(parsedCommand.isEmpty());
    }
}