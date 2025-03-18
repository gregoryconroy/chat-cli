package com.cli.chat.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.Message;
import com.cli.chat.util.Delay;
import com.cli.chat.util.LoadingAnimation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.cli.chat.models.records.User;

public class ApiHandler {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_URL = "http://ec2-13-246-228-91.af-south-1.compute.amazonaws.com:8080/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<User> getUsers() {
        LoadingAnimation.startLoadingAnimation("Retrieving user list");
//        List<User> users = get("user/list", new TypeReference<>() {});
        List<User> users = getFile("src/main/java/com/cli/chat/data/users.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return users;
    }

    public static List<Message> getMessages(String username) {
        LoadingAnimation.startLoadingAnimation("Retrieving messages");
//      List<Message> messages = get("conversation/" + username, new TypeReference<>() {});
        List<Message> messages = getFile("src/main/java/com/cli/chat/data/messages.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return messages;
    }

    public static List<Chat> getChats() {
        LoadingAnimation.startLoadingAnimation("Retrieving chats");
//      List<Chat> chats = get("chats", new TypeReference<>() {});
        List<Chat> chats = getFile("src/main/java/com/cli/chat/data/chats.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return chats;
    }

    public static String getUsername(String username) {
        LoadingAnimation.startLoadingAnimation("Checking if account exists");
        new Delay(2000);
        LoadingAnimation.stopLoadingAnimation();
        return username;
    }

    public static void createAccount(String username, String token) {
        LoadingAnimation.startLoadingAnimation("Creating account");
        try {
            User newUser = new User(username, token);
            
            User createdUser = post("user/create", newUser, new TypeReference<User>() {});
            
            System.out.println("Account created successfully: " + createdUser.username());
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
        LoadingAnimation.stopLoadingAnimation();
    }

    private static <T> T get(String endpoint, TypeReference<T> responseType) {
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

    private static <T, R> R post(String endpoint, T requestBody, TypeReference<R> responseType) {
        HttpRequest request;
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + endpoint))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new IOException("HTTP Error: " + response.statusCode() + " - " + response.body());
            }

            return objectMapper.readValue(response.body(), responseType);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); 
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }

    private static <T> T getFile(String filename, TypeReference<T> responseType) {
        new Delay(2000);
        try {
            return objectMapper.readValue(new java.io.File(filename), responseType);
        } catch (IOException e) {
            throw new RuntimeException("File read failed: " + e.getMessage(), e);
        }
    }

}
