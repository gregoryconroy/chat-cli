package com.cli.chat.models.records;

import java.sql.Date;
import java.sql.Time;

public record Message(String sender, String message, String date, String time) {}