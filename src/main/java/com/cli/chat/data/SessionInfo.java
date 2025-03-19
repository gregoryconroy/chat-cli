package com.cli.chat.data;

public class SessionInfo {
    private static String username;
    private static String jwt;
    private static String activeConversation;

    private SessionInfo() {}

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionInfo.username = username;
    }

    public static String getJWT() {
        return jwt;
    }

    public static void setJWT(String jwt) {
        SessionInfo.jwt = jwt;
    }

    public static String getActiveConversation() {
        return activeConversation;
    }

    public static void setActiveConversation(String currentChat) {
        SessionInfo.activeConversation = currentChat;
    }
}
