package com.cli.chat.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import com.cli.chat.data.SessionInfo;
import com.cli.chat.exception.*;
import com.cli.chat.models.records.*;
import com.cli.chat.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ApiHandler {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_URL = "http://ec2-13-246-228-91.af-south-1.compute.amazonaws.com:8080/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ApiHandler() {}

    public static void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String getUsername() throws Exception {
        LoadingAnimation.startLoadingAnimation("Checking if account exists");
        try {
            User newUser = get("user/check", new TypeReference<>() {}, SessionInfo.getJWT());

            if (Objects.isNull(newUser.username())) {
                throw new UserNotFoundException("User doesn't exist");
            } else {
                return newUser.username();
            }
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static void createAccount(String username, String token) throws Exception {
        LoadingAnimation.startLoadingAnimation("Creating account");
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("username", username);

            post("user/create", requestBody, token);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static List<Conversation> getConversations() throws Exception {
        LoadingAnimation.startLoadingAnimation("Retrieving conversation list");
        try {
            return get("conversations/list", new TypeReference<>() {}, SessionInfo.getJWT());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static List<Message> getMessages(String conversation) throws Exception {
        LoadingAnimation.startLoadingAnimation("Retrieving messages");
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("conversation", conversation);
            return post("conversations/show", requestBody, new TypeReference<>() {}, SessionInfo.getJWT());
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static void sendMessage(String recipient, String message) throws Exception {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("conversation", recipient);
            requestBody.put("text", message);

            post("conversations/message", requestBody, SessionInfo.getJWT());
        } catch (Exception e) {
            throw new Exception("Could not send message" + e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static void createConversation(String conversationName) throws Exception {
        LoadingAnimation.startLoadingAnimation("Creating account");
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", conversationName);

            post("conversations/create", requestBody, SessionInfo.getJWT());
        } catch (Exception e) {
            throw new Exception("Could not create conversation" + e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static void addUserToConversation(String name, String conversationName) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("username", name);
            requestBody.put("conversationName", conversationName);

            post("conversations/add-user", requestBody, SessionInfo.getJWT());
        } catch (Exception e) {
            ConsolePrinter.println("Error adding user to conversation: " + e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    public static List<User> getUsers() throws Exception {
        LoadingAnimation.startLoadingAnimation("Retrieving user list");
        try {
            return get("user/list", new TypeReference<>() {}, SessionInfo.getJWT());
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    private static <T> T get(String endpoint, TypeReference<T> responseType, String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + endpoint))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP Error: " + response.statusCode() + " - " + response.body());
            }

            try {
                return objectMapper.readValue(response.body(), responseType);
            } catch (JsonProcessingException e) {
                throw new ApiResponseParsingException("Failed to parse JSON response");
            }

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); 
            }
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }
    
    private static <T> void post(String endpoint, T requestBody, String token) {
        HttpRequest request;
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + endpoint))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
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

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }

    private static <T, R> R post(String endpoint, T requestBody, TypeReference<R> responseType, String token) {
        HttpRequest request;
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + endpoint))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
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
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }
}
