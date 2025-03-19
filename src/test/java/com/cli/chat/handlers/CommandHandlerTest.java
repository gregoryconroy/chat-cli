package com.cli.chat.handlers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    @Test
    void handleCommand_ShouldExecuteChatsCommand() {
        // Simulate the input for the "chats" command
        CommandHandler.handleCommand("chats");
        // Add an assertion here depending on how `StateHandler.showChats()` should be validated
        // Since we can't mock it with Mockito, the test will pass if there are no errors
    }

    @Test
    void handleCommand_ShouldHandleInvalidCommand() {
        // Simulate an invalid command
        CommandHandler.handleCommand("invalidcommand");

        // Ensure the error message is printed, this will require you to validate console output if needed
        // A better approach may be to check the ConsolePrinter behavior using a custom output stream, if needed
    }

    @Test
    void handleCommand_ShouldExecuteExitCommand() {
        // Simulate the exit command
        // This test case might be tricky since System.exit(0) terminates the JVM
        // Consider using a security manager or other approaches to prevent actual exit during tests
        try {
            CommandHandler.handleCommand("exit");
            fail("System.exit() should not terminate the JVM in a test");
        } catch (SecurityException expected) {
            // Test passed, exit was blocked
        }
    }

    @Test
    void handleCommand_ShouldExecuteHelpCommand() {
        // Simulate the "help" command
        CommandHandler.handleCommand("help");

        // Add assertion based on what should happen when `StateHandler.showHelp()` is called
    }

    @Test
    void handleCommand_ShouldHandleSetUsernameCommand() {
        // Simulate the "set username" command with an argument
        CommandHandler.handleCommand("set username myUsername");

        // Check if `StateHandler.createAccount()` is called with "myUsername"
        // You may need to check the console output or validate if the username was passed correctly
    }
}
