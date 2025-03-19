package com.cli.chat.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cli.chat.data.SessionInfo;
import com.cli.chat.dto.DirectMessageDTO;
import com.cli.chat.exception.ApiResponseParsingException;
import com.cli.chat.exception.UserNotFoundException;
import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.Conversation;
import com.cli.chat.models.records.Message;
import com.cli.chat.util.ConsolePrinter;
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
        LoadingAnimation.startLoadingAnimation("Retrieving conversation list");
//        List<Chat> chats = get("chats", new TypeReference<>() {});
        List<Chat> chats = getFile("src/main/java/com/cli/chat/data/chats.json", new TypeReference<>() {});
        LoadingAnimation.stopLoadingAnimation();
        return chats;
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

    public static void sendMessage(String recipient, String message) throws Exception {

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("conversationName", recipient);
            requestBody.put("message", message);

            post("conversations/send", requestBody, SessionInfo.getJWT());
        } catch (Exception e) {
            throw new Exception("Could not send message" + e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }

    }

    public static void addConvoUser(String name, String conversationName) {
        try {
            List<User> users = getUsers();
            
            boolean userExists = users.stream().anyMatch(user -> user.username().equalsIgnoreCase(name));
            
            if (!userExists) {
                ConsolePrinter.println("User '" + name + "' not found.");
                return;
            }
            var requestBody = new Object() {
                public final String username = name;
                public final String conversationname = conversationName;
            };
            
            post("conversation/user/add", requestBody, SessionInfo.getJWT());
            
            ConsolePrinter.println("User '" + name + "' has been added to conversation: " + conversationName);
        } catch (Exception e) {
            ConsolePrinter.println("Error adding user to conversation: " + e.getMessage());
        } finally {
            LoadingAnimation.stopLoadingAnimation();
        }
    }

    private static <T> T get(String endpoint, TypeReference<T> responseType) throws Exception {
        return get(endpoint, responseType, "");
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
                Thread.currentThread().interrupt(); // Preserve interruption status
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


    private static <T> T getFile(String filename, TypeReference<T> responseType) {
        new Delay(1000);
        try {
            return objectMapper.readValue(new java.io.File(filename), responseType);
        } catch (IOException e) {
            throw new RuntimeException("File read failed: " + e.getMessage(), e);
        }
    }

}
