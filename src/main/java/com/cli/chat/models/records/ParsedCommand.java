package com.cli.chat.models.records;

import com.cli.chat.models.enums.Command;

import java.util.List;

public record ParsedCommand(Command command, List<String> arguments) {}