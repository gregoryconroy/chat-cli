package com.cli.chat.handlers;

import com.cli.chat.models.enums.Command;
import com.cli.chat.models.records.ParsedCommand;
import com.cli.chat.util.ConsolePrinter;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.*;

public class CommandHandler {

    private CommandHandler() {}

    public static void handleCommand(String input) {
        Optional<ParsedCommand> optionalParsedCommand = parseCommand(input);

        optionalParsedCommand.ifPresentOrElse(
                CommandHandler::executeCommand,
                CommandHandler::handleInvalidCommand
        );
    }

    private static Optional<ParsedCommand> getParsedCommand(Command command, String input) {
        Matcher matcher = command.getPattern().matcher(input);
        return matcher.matches()
                ? Optional.of(new ParsedCommand(command,
                IntStream.rangeClosed(1, matcher.groupCount())
                        .mapToObj(matcher::group)
                        .map(String::trim)
                        .collect(Collectors.toList()))
        )
                : Optional.empty();
    }

    private static Optional<ParsedCommand> parseCommand(String input) {
        List<Command> availableCommands = StateHandler.getAvailableCommands();

        return availableCommands.stream()
                .filter(command -> command.getPattern().matcher(input).matches())
                .findFirst()
                .flatMap(command -> getParsedCommand(command, input));
    }

    private static void executeCommand(ParsedCommand parsedCommand) {
        Command command = parsedCommand.command();
        List<String> arguments = parsedCommand.arguments();

        switch (command) {
            case CHATS -> StateHandler.showConversations();
            case CREATE_CONVO -> StateHandler.createConversation(arguments.getFirst());
            case EXIT -> System.exit(0);
            case HELP -> StateHandler.showHelp();
            case SET_USERNAME -> StateHandler.createAccount(arguments.getFirst());
            case LOGOUT -> StateHandler.logout();
            case TIPS -> StateHandler.setTips(arguments.getFirst());
            case OPEN_CONVO -> StateHandler.openConversation(arguments.getFirst());
            case ADD_CONVO_USER -> StateHandler.addUserToConversation(arguments.getFirst());
            case REFRESH -> StateHandler.refresh();
            case SEND_MESSAGE -> StateHandler.sendMessage(arguments.getFirst());
            case USERS -> StateHandler.showUsers();
        }
    }

    private static void handleInvalidCommand() {
        ConsolePrinter.blankln();
        ConsolePrinter.print("Invalid command", ConsolePrinter.RED, ConsolePrinter.BOLD);
        ConsolePrinter.print(" - type ");
        ConsolePrinter.print("help", ConsolePrinter.GREEN, ConsolePrinter.BOLD);
        ConsolePrinter.println(" for a list of available commands");
        ConsolePrinter.blankln();
    }
}