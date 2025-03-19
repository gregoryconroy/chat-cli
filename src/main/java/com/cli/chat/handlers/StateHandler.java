package com.cli.chat.handlers;

import com.cli.chat.data.SessionInfo;
import com.cli.chat.exception.UserNotFoundException;
import com.cli.chat.models.enums.Command;
import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.Message;
import com.cli.chat.models.records.User;
import com.cli.chat.util.ConsolePrinter;
import com.cli.chat.models.enums.Page;
import com.cli.chat.util.Delay;
import com.cli.chat.util.LoadingAnimation;

import java.util.List;

public class StateHandler {
    private static Page currentPage;
    private static boolean showTips = false;

    public static void init() {
        StateHandler.gotoPage(Page.LOGIN);
        String token = BrowserHandler.getToken();
        SessionInfo.setJWT(token);

        try {
            String username = ApiHandler.getUsername();
            SessionInfo.setUsername(username);
            showWelcome();
        } catch (UserNotFoundException e) {
            ConsolePrinter.println(e.getMessage(), ConsolePrinter.BLUE);
            new Delay(2000);
            StateHandler.showSignUp();
        } catch (Exception e) {
            ConsolePrinter.println(e.getMessage(), ConsolePrinter.RED, ConsolePrinter.BOLD, ConsolePrinter.UNDERLINE);
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
            ConsolePrinter.println(e.getMessage(), ConsolePrinter.RED, ConsolePrinter.BOLD, ConsolePrinter.UNDERLINE);
            new Delay(10000);
            showSignUp();
        }
    }

    public static void showWelcome() {
        ConsolePrinter.print("Welcome ");
        ConsolePrinter.print(SessionInfo.getUsername(), ConsolePrinter.BLUE, ConsolePrinter.BOLD);
        ConsolePrinter.print("!");
        new Delay(3000);
        showChats();
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
           showConversation(conversationName);
        } catch (Exception e) {
            ConsolePrinter.println(e.getMessage(), ConsolePrinter.RED, ConsolePrinter.BOLD, ConsolePrinter.UNDERLINE);
        }
    }

    public static void showChats() {
        StateHandler.gotoPage(Page.CHATS);
        List<Chat> chats = ApiHandler.getChats();
        ConsolePrinter.printChats(chats);
    }

    public static void showConversation(String conversationName) {
        gotoPage(Page.CONVERSATION);

        SessionInfo.setCurrentChat(conversationName);
        
        ConsolePrinter.print("Conversation with: ");
        ConsolePrinter.println(conversationName, ConsolePrinter.BLUE, ConsolePrinter.BOLD);
        ConsolePrinter.blankln();

        List<Message> messages = ApiHandler.getMessages(conversationName);
        ConsolePrinter.printConversation(messages);
    }

    public static void deleteChats(){
        ApiHandler.deleteChat();
    }

    public static void sendMessage(String message) {
        ApiHandler.sendMessage(SessionInfo.getUsername(), SessionInfo.getCurrentChat(), message, SessionInfo.getJWT());
    }

    public static void showHelp() {
        ConsolePrinter.blankln();
        ConsolePrinter.printPageHelp(currentPage);
    }

    public static void showUsers() {
        List<User> users = ApiHandler.getUsers();
        ConsolePrinter.printUsers(users);
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