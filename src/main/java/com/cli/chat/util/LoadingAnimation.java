package com.cli.chat.util;

import java.util.concurrent.*;

public class LoadingAnimation {
    private static volatile boolean running = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String LOAD_COLOUR = ConsolePrinter.CYAN;

    public static void startLoadingAnimation(String info) {
        if (running) return; // Prevent multiple animations
        running = true;

        executor.submit(() -> {
            int underlineLength = 3;
            int underlineStartIndex = 0;
            int underlineEndIndex = 0;

            System.out.print("\033[?25l"); // Hide cursor

            while (running) {
                System.out.print("\r");

                for (int i = 0; i < info.length(); i++) {
                    if (i >= underlineStartIndex && i < underlineEndIndex) {
                        ConsolePrinter.print(info.substring(i, i + 1), LOAD_COLOUR);
                    } else {
                        ConsolePrinter.print(info.substring(i, i + 1));
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

                new Delay(50);
            }

            System.out.print("\033[?25h"); // Show cursor
        });
    }

    public static void stopLoadingAnimation() {
        running = false;
        new Delay(60);
        clearText();
    }

    private static void clearText() {
        // Move to the beginning of the line, clear it, and reset cursor
        System.out.print("\r\033[K");
    }
}
