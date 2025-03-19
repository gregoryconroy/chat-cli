package com.cli.chat.models.records;

import java.time.LocalDateTime;

public record Message(String username, String message, LocalDateTime datetime) {}