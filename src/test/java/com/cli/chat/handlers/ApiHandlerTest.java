package com.cli.chat.handlers;

import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.Message;
import com.cli.chat.models.records.User;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ApiHandlerTest {

    @Test
    void getUsers_ShouldReturnListOfUsers() {
        List<User> users = ApiHandler.getUsers();

        assertNotNull(users, "User list should not be null");
        assertFalse(users.isEmpty(), "User list should not be empty");
    }

    @Test
    void getMessages_ShouldReturnListOfMessages() {
        List<Message> messages = ApiHandler.getMessages("testUser");

        assertNotNull(messages, "Messages should not be null");
        assertFalse(messages.isEmpty(), "Messages should not be empty");
    }

    @Test
    void getChats_ShouldReturnListOfChats() {
        List<Chat> chats = ApiHandler.getChats();

        assertNotNull(chats, "Chat list should not be null");
        assertFalse(chats.isEmpty(), "Chat list should not be empty");
    }

    @Test
    void getUsername_ShouldReturnSameUsername() throws Exception {
        // Given that the API returns a user with a valid username,
        // you can assume that the method should return this username.

        // Example to check if the returned username is not null
        String result = ApiHandler.getUsername();  // No arguments passed now.

        assertNotNull(result, "Username should not be null");
        // Optionally add more assertions based on the actual logic, like checking the specific username
    }

    @Test
    void createAccount_ShouldNotThrowExceptions() {
        assertDoesNotThrow(() -> ApiHandler.createAccount("testUser", "testToken"));
    }

    @Test
    void sendMessage_ShouldNotThrowExceptions() {
        assertDoesNotThrow(() -> ApiHandler.sendMessage("Alice", "Bob"));
    }
}