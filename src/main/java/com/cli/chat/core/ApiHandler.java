package com.cli.chat.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.Message;
import com.cli.chat.util.ConsolePrinter;
import com.cli.chat.util.LoadingAnimation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.cli.chat.models.records.User;

public class ApiHandler {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_URL = "http://ec2-13-246-228-91.af-south-1.compute.amazonaws.com:8080/api/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<User> getUsers() {
//        return get("user/list", new TypeReference<>() {});
        LoadingAnimation.startLoadingAnimation("Retrieving user list");
        List<User> users = getFile("src/main/java/com/cli/chat/data/users.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return users;
    }

    public static List<Message> getMessages(String username) {
//        return get("conversation/" + username, new TypeReference<>() {});
        LoadingAnimation.startLoadingAnimation("Retrieving messages");
        List<Message> messages = getFile("src/main/java/com/cli/chat/data/messages.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return messages;
    }

    public static List<Chat> getChats() {
//        return get("chats", new TypeReference<>() {});
        LoadingAnimation.startLoadingAnimation("Retrieving chats");
        List<Chat> chats = getFile("src/main/java/com/cli/chat/data/chats.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return chats;

    }

    private static <T> T get(String endpoint, TypeReference<T> responseType) {
        ConsolePrinter.println("In get", ConsolePrinter.RED, ConsolePrinter.BOLD);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP Error: " + response.statusCode() + " - " + response.body());
            }

            return objectMapper.readValue(response.body(), responseType);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status if interrupted
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }

    private static <T> T getFile(String filename, TypeReference<T> responseType) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            return objectMapper.readValue(new java.io.File(filename), responseType);
        } catch (IOException e) {
            throw new RuntimeException("File read failed: " + e.getMessage(), e);
        }
    }

}
