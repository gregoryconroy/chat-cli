package com.cli.chat.models.records;

import java.time.LocalDateTime;

public record Conversation(String conversationName, String lastSenderUsername, String lastMessage, LocalDateTime datetime) {}
