package com.cli.chat.util;

import com.cli.chat.data.SessionInfo;
import com.cli.chat.models.enums.*;
import com.cli.chat.models.records.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class ConsolePrinter {

    // Text color codes
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Background color codes
    public static final String BLACK_BG = "\u001B[40m";
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String MAGENTA_BG = "\u001B[45m";
    public static final String CYAN_BG = "\u001B[46m";
    public static final String WHITE_BG = "\u001B[47m";

    // Text style codes
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String INVERTED = "\u001B[7m";

    public static void print(String text, String... styles) {
        StringBuilder formattedText = new StringBuilder();

        for (String style : styles) {
            formattedText.append(style);
        }

        formattedText.append(text).append(RESET);

        System.out.print(formattedText);
    }

    public static void println(String text, String... styles) {
        StringBuilder formattedText = new StringBuilder();

        for (String style : styles) {
            formattedText.append(style);
        }

        formattedText.append(text).append(RESET);

        System.out.println(formattedText);
    }

    public static void blankln() {
        System.out.println();
    }

    public static void printCommand(Command command) {
        String paramString = String.join(" ", command.getParams());

        print(command.getName(), BLUE, BOLD);

        if (!paramString.isEmpty()) {
            print(" ");
            print(paramString, MAGENTA);
        }

        print(" - ");
        println(command.getDescription(), GREEN);
    }

    public static void printPageHelp(Page page) {
        List<Command> commands = page.getAvailableCommands();
        commands.sort(Comparator.comparing(Command::getName));

        print("Available commands for the ");
        print(page.getDisplayName() + " Page", BLUE, BOLD);
        println(":");
        blankln();
        commands.forEach(ConsolePrinter::printCommand);
        blankln();
    }

    public static void printPage(String displayName) {
        blankln();
        print("--- ", BOLD);
        print(displayName, BLUE, BOLD);
        println(" ---", BOLD);
        blankln();
    }

    public static void printConversation(List<Message> messages) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Optional.of(messages)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(message -> {
                            print(Objects.equals(message.username(), SessionInfo.getUsername()) ? "You" : message.username(),
                                    Objects.equals(message.username(), SessionInfo.getUsername()) ? CYAN : BLUE, BOLD);
                            println(" [" + message.datetime().toLocalTime().format(formatter) + "]", YELLOW);
                            println(message.message());
                            blankln();
                        }),
                        () -> {
                            println("No messages in this conversation.", RED);
                            blankln();
                        }
                );
    }


    public static void printConversations(List<Conversation> conversations) {
        Queue<String> colourQueue = new LinkedList<>();
        colourQueue.add(BLUE);
        colourQueue.add(MAGENTA);
        colourQueue.add(CYAN);

        if (conversations.isEmpty()) {
            println("No conversations exist", RED);
        } else {
            conversations.forEach(conversation -> {
                String colour = colourQueue.remove();
                print(conversation.conversationName(), colour, BOLD);

                if (!Objects.isNull(conversation.datetime())) {
                    println(" [" + conversation.datetime().toLocalDate() + "]", YELLOW);
                    print(conversation.lastSenderUsername() + ": ", GREEN, BOLD);
                    println(conversation.lastMessage());
                }
                else {
                    println("\nNo messages in this conversation", RED);
                }

                colourQueue.add(colour);
                blankln();
            });
        }
    }

    public static void printError(String error) {
        println(error, RED, BOLD, UNDERLINE);
    }

    public static void printUsers(List<User> users) {
        users.forEach(user -> {
            println(user.username(), BLUE, BOLD);
            println(user.email(), GREEN);
            blankln();
        });
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Error clearing console: " + e.getMessage());
        }
    }
}