package com.cli.chat.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Page {
    LOGIN("Login"),
    SIGN_UP("Sign Up"),
    CHATS("Chats"),
    SETTINGS("Settings"),
    CONVERSATION("Conversation");

    private final String displayName;

    Page(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static List<Command> getCommonCommands() {
        return List.of(Command.EXIT, Command.HELP, Command.TIPS);
    }

    public List<Command> getCurrentCommands() {
        return switch (this) {
            case LOGIN -> List.of(Command.LOGIN);
            case CHATS -> List.of(Command.OPEN_CHAT, Command.PAGE_UP, Command.PAGE_DOWN, Command.SETTINGS, Command.LOGOUT);
            case SIGN_UP -> List.of();
            case SETTINGS -> List.of(Command.BACK, Command.LOGOUT);
            case CONVERSATION -> List.of(Command.SEND, Command.PAGE_UP, Command.PAGE_DOWN, Command.DELETE_CHAT, Command.BACK, Command.LOGOUT);
        };
    }

    public List<Command> getAvailableCommands() {
        List<Command> common = getCommonCommands();
        List<Command> specific = getCurrentCommands();

        List<Command> combined = new ArrayList<>(specific);
        combined.addAll(common);

        return combined;
    }
}