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
    private final static Stack<Page> pageStack = new Stack<>();

    public static void init() {
        StateHandler.gotoPage(Page.LOGIN);
    }

    public static void gotoPage(Page page) {
        currentPage = page;
        pageStack.push(currentPage);
        showPageInfo();

        switch (currentPage) {
            case Page.CHATS -> showChats();
        }
    }

    private static void showChats() {
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

    public static void prevPage() {
        pageStack.pop();
        gotoPage(pageStack.pop());
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
        gotoPage(Page.CHATS);
    }

    public static void logout() {
        LoadingAnimation.startLoadingAnimation("Logging out");
        LoadingAnimation.stopLoadingAnimation();
        pageStack.clear();
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