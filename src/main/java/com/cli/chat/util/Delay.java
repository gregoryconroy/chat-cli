package com.cli.chat.util;

public record Delay(int millis) {
    public Delay {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
