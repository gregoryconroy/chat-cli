package com.cli.chat.util;

import com.cli.chat.models.records.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonConverter {
    public static List<Message> extractMessages(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            // Define the type for List<Message>
            Type messageListType = new TypeToken<List<Message>>() {}.getType();
            // Deserialize JSON into List<Message>
            return gson.fromJson(reader, messageListType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read or parse the JSON file.", e);
        }
    }
}