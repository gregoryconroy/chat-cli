package com.cli.chat.core;

import com.cli.chat.models.records.Message;
import com.cli.chat.util.ConsolePrinter;
import com.cli.chat.models.enums.Page;
import com.cli.chat.util.JsonConverter;

import java.util.List;
import java.util.Stack;

public class StateHandler {
    private static Page currentPage;
    private static boolean showTips = true;
    private final static Stack<Page> pageStack = new Stack<>();

    public static void gotoPage(Page page) {
        currentPage = page;
        pageStack.push(currentPage);
        showPageInfo();
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
        currentPage = pageStack.peek();
        showPageInfo();
    }

    public static void setTips(String option) {
        if (option.equals("show")) {
            showTips = true;
        }
        else if (option.equals("hide")) {
            showTips = false;
        }
    }

    public static void openConversation(String username) {
        gotoPage(Page.CONVERSATION);

        List<Message> messages = JsonConverter.extractMessages("src/main/java/com/cli/chat/data/messages.json");

        ConsolePrinter.printConversation(messages);
    }

    public static Page getCurrentPage() {
        return currentPage;
    }

    public static void showHelp() {
        ConsolePrinter.println("");
        ConsolePrinter.printPageHelp(currentPage);
    }

    public static void login() {
        ConsolePrinter.showLoading("Logging in", 100);
        gotoPage(Page.CHATS);
    }

    public static void logout() {
        ConsolePrinter.showLoading("Logging out", 100);
        pageStack.clear();
        gotoPage(Page.LOGIN);
    }
}