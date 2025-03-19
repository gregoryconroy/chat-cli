package com.cli.chat.data;

public class SessionInfo {
    private static String username;
    private static String JWT;
    private static String activeConversation;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionInfo.username = username;
    }

    public static String getJWT() {
        return JWT;
    }

    public static void setJWT(String JWT) {
        SessionInfo.JWT = JWT;
    }

    public static String getActiveConversation() {
        return activeConversation;
    }

    public static void setActiveConversation(String currentChat) {
        SessionInfo.activeConversation = currentChat;
    }
}
