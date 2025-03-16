package com.cli.chat.util;

import com.cli.chat.models.enums.Command;
import com.cli.chat.models.enums.Page;
import com.cli.chat.models.records.Message;

import java.util.Comparator;
import java.util.List;

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

        // Apply styles
        for (String style : styles) {
            formattedText.append(style);
        }

        // Append the text and reset formatting
        formattedText.append(text).append(RESET);

        // Print to the terminal
        System.out.print(formattedText);
    }

    public static void println(String text, String... styles) {
        StringBuilder formattedText = new StringBuilder();

        // Apply styles
        for (String style : styles) {
            formattedText.append(style);
        }

        // Append the text and reset formatting
        formattedText.append(text).append(RESET);

        // Print to the terminal
        System.out.println(formattedText);
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
        println("");
        commands.forEach(ConsolePrinter::printCommand);
        println("");
    }

    public static void printPage(String displayName) {
        println("");
        print("--- ", BOLD);
        print(displayName + " Page", BLUE, BOLD);
        println(" ---", BOLD);
        println("");
    }

    public static void printConversation(List<Message> messages) {
        messages.forEach(message -> {
            print(message.sender(), BLUE, BOLD);
            print(" ");
            println(message.message());
        });
    }

    public static void clearConsole() {
        try {
            // For Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix-based systems
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Error clearing console: " + e.getMessage());
        }
    }

    public static void showLoading(String info, int millis) {
        int underlineLength = 3; // Length of the underline
        long endTime = System.currentTimeMillis() + millis;

        int underlineStartIndex = 0;
        int underlineEndIndex = 0;

        System.out.print("\033[?25l"); // hide cursor

        // Loop to animate the underlining
        while (System.currentTimeMillis() < endTime) {
            System.out.print("\r");

            for (int i = 0; i < info.length(); i++) {
                if (i >= underlineStartIndex && i < underlineEndIndex) {
                    print(info.substring(i, i + 1), CYAN);
                }
                else {
                    print(info.substring(i, i + 1));
                }
            }

            underlineEndIndex++;
            if (underlineEndIndex - underlineStartIndex > underlineLength) {
                underlineStartIndex++;
            }

            if (underlineStartIndex > info.length()) {
                underlineStartIndex = 0;
                underlineEndIndex = 0;
            }

            try {
                Thread.sleep(50); // Adjust the speed of the animation here
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.print("\033[?25h"); // show cursor

        print("\r");
        for (int i = 0; i < info.length(); i++) {
            print(" ");
        }
    }

}