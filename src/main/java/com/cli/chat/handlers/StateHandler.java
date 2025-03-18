package com.cli.chat.handlers;

import com.cli.chat.data.SessionInfo;
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
        String username = ApiHandler.getUsername(null);

        if (username != null) {
            SessionInfo.setUsername(username);
            showWelcome();
        } else {
            StateHandler.showSignUp();
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
        try {
            String token = SessionInfo.getJWT();
            ApiHandler.createAccount(username, token);
            SessionInfo.setUsername(username);
            showWelcome();
        } catch (Exception e) {
            ConsolePrinter.println("Account creation failed: " + e.getMessage());
        }
    }

    public static void showWelcome() {
        ConsolePrinter.clearConsole();
        ConsolePrinter.print("Welcome ");
        ConsolePrinter.print(SessionInfo.getUsername(), ConsolePrinter.BLUE, ConsolePrinter.BOLD);
        ConsolePrinter.println("!");
        new Delay(5000);
        showChats();
    }

    public static void showChats() {
        StateHandler.gotoPage(Page.CHATS);
        List<Chat> chats = ApiHandler.getChats();
        ConsolePrinter.printChats(chats);
        System.out.println(SessionInfo.getJWT());

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

    public static void login() {
        showChats();
    }

    public static void logout() {
        LoadingAnimation.startLoadingAnimation("Logging out");
        LoadingAnimation.stopLoadingAnimation();
        gotoPage(Page.LOGIN);
    }

    public static void openConversation(String username) {
        gotoPage(Page.CONVERSATION);

        ConsolePrinter.print("Conversation with: ");
        ConsolePrinter.println(username, ConsolePrinter.BLUE, ConsolePrinter.BOLD);
        ConsolePrinter.println("");

        List<Message> messages = ApiHandler.getMessages(username);
        ConsolePrinter.printConversation(messages);
    }

    public static void showHelp() {
        ConsolePrinter.println("");
        ConsolePrinter.printPageHelp(currentPage);
    }

    public static void showUsers() {
        List<User> users = ApiHandler.getUsers();
        ConsolePrinter.printUsers(users);
    }

    public static List<Command> getAvailableCommands() {
        return currentPage.getAvailableCommands();
    }
}