package com.cli.chat.models.enums;

import java.util.List;

import java.util.regex.Pattern;

public enum Command {
    SEND("send", List.of("<message>"), "send a message to the current user", "^send\\s+(.+)$"),
    HELP("help", List.of(), "show a list of available commands and their descriptions", "^help$"),
    EXIT("exit", List.of(), "terminate the application", "^exit$"),
    BACK("back", List.of(), "return to the previous page", "^back$"),
    LOGIN("login", List.of(), "request external login authorisation", "^login$"),
    LOGOUT("logout", List.of(), "log out of application", "^logout"),
    PAGE_UP("pageup", List.of(), "go up a page", "^pageup$"),
    SETTINGS("settings", List.of(), "go to settings page", "^settings$"),
    PAGE_DOWN("pagedown", List.of(), "go down a page", "^pageup$"),
    OPEN_CHAT("openchat", List.of("<username>"), "open the conversation between you and the specified user", "^openchat\\s+([\\w-]+)$"),
    DELETE_CHAT("deletechat", List.of(), "delete the chat history between you and the specified user", "^deletechat$"),
    TIPS("tips", List.of("[show|hide]"), "show or hide available commands for a page", "^tips\\s+(show|hide)$");

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