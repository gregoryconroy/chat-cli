package com.cli.chat.models.enums;

import java.util.List;

import java.util.regex.Pattern;

public enum Command {
    CHATS("chats", List.of(), "go to the chats page", "^chats$"),
    DELETE_CHAT("deletechat", List.of(), "delete the chat history between you and the specified user", "^deletechat$"),
    EXIT("exit", List.of(), "terminate the application", "^exit$"),
    HELP("help", List.of(), "show a list of available commands and their descriptions", "^help$"),
    LOGIN("login", List.of(), "request external login authorisation", "^login$"),
    LOGOUT("logout", List.of(), "log out of application", "^logout"),
    OPEN_CHAT("openchat", List.of("<username>"), "open the conversation between you and the specified user", "^openchat\\s+([\\w-]+)$"),
    PAGE_DOWN("pagedown", List.of(), "go down a page", "^pageup$"),
    PAGE_UP("pageup", List.of(), "go up a page", "^pageup$"),
    SEND("send", List.of("<message>"), "send a message to the current user", "^send\\s+(.+)$"),
    SETTINGS("settings", List.of(), "go to settings page", "^settings$"),
    TIPS("tips", List.of("[show|hide]"), "show or hide available commands for a page", "^tips\\s+(show|hide)$"),
    USERS("users", List.of(), "show list of all registerd users", "^users$");

    private final String name;
    private final List<String> params;
    private final String description;
    private final Pattern pattern;

    Command(String name, List<String> params, String description, String regex) {
        this.name = name;
        this.params = params;
        this.description = description;
        this.pattern = Pattern.compile(regex);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getParams() {
        return this.params;
    }

    public String getDescription() {
        return this.description;
    }

    public Pattern getPattern() {
        return this.pattern;
    }
}