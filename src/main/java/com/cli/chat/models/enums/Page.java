package com.cli.chat.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Page {
    LOGIN("Login"),
    SIGN_UP("Sign Up"),
    CONVERSATION_LIST("Conversation List"),
    CONVERSATION("Conversation"),
    USER_LIST("User List");


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
            case LOGIN -> List.of();
            case CONVERSATION_LIST -> List.of(Command.OPEN_CONVO, Command.LOGOUT, Command.USERS, Command.CREATE_CONVO, Command.REFRESH);
            case SIGN_UP -> List.of(Command.SET_USERNAME);
            case CONVERSATION -> List.of(Command.ADD_CONVO_USER, Command.CHATS, Command.USERS, Command.SEND_MESSAGE, Command.DELETE_CHAT, Command.LOGOUT, Command.REFRESH);
            case USER_LIST -> List.of(Command.CHATS);
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