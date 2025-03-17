package com.cli.chat.handlers;

import com.cli.chat.models.enums.Command;
import com.cli.chat.models.records.Chat;
import com.cli.chat.models.records.Message;
import com.cli.chat.models.records.User;
import com.cli.chat.util.ConsolePrinter;
import com.cli.chat.models.enums.Page;
import com.cli.chat.util.LoadingAnimation;

import java.util.List;
import java.util.Stack;

public class StateHandler {
    private static Page currentPage;
    private static boolean showTips = false;

    public static void init() {
        StateHandler.gotoPage(Page.LOGIN);
    }

    public static void gotoPage(Page page) {
        currentPage = page;
        showPageInfo();
    }

    public static void showChats() {
        StateHandler.gotoPage(Page.CHATS);
        List<Chat> chats = ApiHandler.getChats();
        ConsolePrinter.printChats(chats);
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