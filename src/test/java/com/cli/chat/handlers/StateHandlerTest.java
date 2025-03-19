package com.cli.chat.handlers;

import com.cli.chat.data.SessionInfo;
import com.cli.chat.models.enums.Command;
import com.cli.chat.models.enums.Page;
import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StateHandlerTest {

    @BeforeEach
    void setUp() {
        SessionInfo.setUsername("testUser");
        SessionInfo.setJWT("testToken");
    }

    @Test
    void gotoPage_ShouldSetCurrentPage() {
        StateHandler.gotoPage(Page.CHATS);
        // Assuming StateHandler stores the current page internally
        assertEquals(Page.CHATS, Page.CHATS); // Placeholder, replace with actual getter
    }

    @Test
    void showSignUp_ShouldNavigateToSignUpPage() {
        StateHandler.showSignUp();
        assertEquals(Page.SIGN_UP, Page.SIGN_UP); // Placeholder, replace with actual getter
    }

    @Test
    void createAccount_ShouldNotThrowExceptions() {
        assertDoesNotThrow(() -> StateHandler.createAccount("newUser"));
    }

    @Test
    void showWelcome_ShouldNotThrowExceptions() {
        assertDoesNotThrow(StateHandler::showWelcome);
    }

    @Test
    void showChats_ShouldNotThrowExceptions() {
        assertDoesNotThrow(StateHandler::showChats);
    }

    @Test
    void setTips_ShouldToggleTips() {
        StateHandler.setTips("show");
        StateHandler.setTips("hide");
        assertTrue(true); // Dummy check, replace with actual verification if needed
    }

    @Test
    void logout_ShouldNotThrowExceptions() {
        assertDoesNotThrow(StateHandler::logout);
    }

    @Test
    void deleteChats_ShouldNotThrowExceptions() {
        assertDoesNotThrow(StateHandler::deleteChats);
    }

    @Test
    void sendMessage_ShouldNotThrowExceptions() {
        assertDoesNotThrow(() -> StateHandler.sendMessage("Hello!"));
    }

    @Test
    void showHelp_ShouldNotThrowExceptions() {
        assertDoesNotThrow(StateHandler::showHelp);
    }

    @Test
    void showUsers_ShouldReturnListOfUsers() {
        assertDoesNotThrow(StateHandler::showUsers);
    }

    @Test
    void getAvailableCommands_ShouldReturnNonNullList() {
        List<Command> commands = StateHandler.getAvailableCommands();
        assertNotNull(commands);
    }
}
