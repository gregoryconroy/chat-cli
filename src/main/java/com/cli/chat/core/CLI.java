package com.cli.chat.core;

import com.cli.chat.util.ConsolePrinter;
import com.cli.chat.models.enums.Page;

import java.util.Scanner;

public class CLI {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        StateHandler.gotoPage(Page.LOGIN);

        while (true) {
            ConsolePrinter.print(">", ConsolePrinter.BLUE);
            String input = scanner.nextLine().trim();

            try {
                CommandHandler.handleCommand(input);
            } catch (Exception e) {
                ConsolePrinter.print("An error occurred: ", ConsolePrinter.RED);
            }
        }

    }


}