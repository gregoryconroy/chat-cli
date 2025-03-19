package com.cli.chat.handlers;

import com.cli.chat.data.SessionInfo;
import com.cli.chat.exception.UserNotFoundException;
import com.cli.chat.models.enums.*;
import com.cli.chat.models.records.*;
import com.cli.chat.util.*;

import java.util.List;

public class StateHandler {
    private static Page currentPage;
    private static boolean showTips = true;

    private StateHandler() {}

    public static void init() {
        StateHandler.gotoPage(Page.LOGIN);
        String token = BrowserHandler.getToken();
        SessionInfo.setJWT(token);
        ApiHandler.init();

        try {
            String username = ApiHandler.getUsername();
            SessionInfo.setUsername(username);
            showWelcome();
        } catch (UserNotFoundException e) {
            ConsolePrinter.println(e.getMessage(), ConsolePrinter.BLUE);
            new Delay(2000);
            StateHandler.showSignUp();
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
            new Delay(5000);
            gotoPage(Page.LOGIN);
        }
    }

    public static void gotoPage(Page page) {
        currentPage = page;
        showPageInfo();
    }

    public static void showSignUp() {
        gotoPage(Page.SIGN_UP);
    }

    public static void createAccount(String username) {
        String token = SessionInfo.getJWT();

        try {
            ApiHandler.createAccount(username, token);
            SessionInfo.setUsername(username);
            showWelcome();
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
            new Delay(10000);
            showSignUp();
        }
    }

    public static void showWelcome() {
        ConsolePrinter.print("Welcome ");
        ConsolePrinter.print(SessionInfo.getUsername(), ConsolePrinter.BLUE, ConsolePrinter.BOLD);
        ConsolePrinter.print("!");
        new Delay(3000);
        showConversations();
    }

    private static void showPageInfo() {
        ConsolePrinter.clearConsole();
        ConsolePrinter.printPage(currentPage.getDisplayName());

        if (showTips) {
            ConsolePrinter.printPageHelp(currentPage);
        }
    }

    public static void setTips(String option) {
        if (option.equals("show")) {
            showTips = true;
        }
        else if (option.equals("hide")) {
            showTips = false;
        }
    }

    public static void createConversation(String conversationName) {
        try {
           ApiHandler.createConversation(conversationName);
           openConversation(conversationName);
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }
        
    public static void addUserToConversation(String name) {
        try {
            ApiHandler.addUserToConversation(name, SessionInfo.getActiveConversation());
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }

    public static void showConversations() {
        StateHandler.gotoPage(Page.CONVERSATION_LIST);

        try {
            List<Conversation> chats = ApiHandler.getConversations();
            ConsolePrinter.printConversations(chats);
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }

    public static void openConversation(String conversationName) {
        try {
            List<Message> messages = ApiHandler.getMessages(conversationName);

            gotoPage(Page.CONVERSATION);

            SessionInfo.setActiveConversation(conversationName);

            ConsolePrinter.println(conversationName, ConsolePrinter.YELLOW, ConsolePrinter.BOLD, ConsolePrinter.UNDERLINE);
            ConsolePrinter.blankln();

            ConsolePrinter.printConversation(messages);
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }

    public static void sendMessage(String message) {
        try {
            ApiHandler.sendMessage(SessionInfo.getActiveConversation(), message);
            refresh();
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }

    public static void refresh(){
        switch (currentPage) {
            case Page.CONVERSATION_LIST -> showConversations();
            case Page.CONVERSATION -> openConversation(SessionInfo.getActiveConversation());
        }
    }

    public static void showHelp() {
        ConsolePrinter.blankln();
        ConsolePrinter.printPageHelp(currentPage);
    }

    public static void showUsers() {
        try {
            List<User> users = ApiHandler.getUsers();

            gotoPage(Page.USER_LIST);
            ConsolePrinter.printUsers(users);
        } catch (Exception e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }

    public static void logout() {
        LoadingAnimation.startLoadingAnimation("Logging out");
        LoadingAnimation.stopLoadingAnimation();
        gotoPage(Page.LOGIN);
    }

    public static List<Command> getAvailableCommands() {
        return currentPage.getAvailableCommands();
    }
}