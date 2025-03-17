package com.cli.chat.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Page {
    LOGIN("Login"),
    SIGN_UP("Sign Up"),
    CHATS("Chats"),
    CONVERSATION("Conversation");

    private final String displayName;

    Page(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static List<Command> getGlobalCommands() {
        return List.of(Command.EXIT, Command.HELP, Command.TIPS);
    }

    public List<Command> getCurrentCommands() {
        return switch (this) {
            case LOGIN -> List.of(Command.LOGIN);
            case CHATS -> List.of(Command.OPEN_CHAT, Command.PAGE_UP, Command.PAGE_DOWN, Command.LOGOUT, Command.USERS);
            case SIGN_UP -> List.of();
            case CONVERSATION -> List.of(Command.CHATS, Command.SEND, Command.PAGE_UP, Command.PAGE_DOWN, Command.DELETE_CHAT, Command.LOGOUT);
        };
    }

    public List<Command> getAvailableCommands() {
        List<Command> common = getGlobalCommands();
        List<Command> specific = getCurrentCommands();

        List<Command> combined = new ArrayList<>(specific);
        combined.addAll(common);

        return combined;
    }
}