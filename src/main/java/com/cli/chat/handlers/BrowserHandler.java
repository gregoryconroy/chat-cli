package com.cli.chat.handlers;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

public class BrowserHandler {
    private static final String AUTH_URL = "http://ec2-13-246-228-91.af-south-1.compute.amazonaws.com:8080/api/token";
    private static CompletableFuture<String> authCodeFuture;

    public static CompletableFuture<String> login() {
        authCodeFuture = new CompletableFuture<>();
        startRedirectListener();
        openLoginPage();
        return authCodeFuture;
    }

    public static void openLoginPage() {
        try {
            String url = AUTH_URL;

            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRedirectListener() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/login/oauth2/code/google", BrowserHandler::handleRedirect);
            server.start();
            System.out.println("Listening for OAuth2 redirect on http://localhost:8080/login/oauth2/code/google...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRedirect(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        System.out.println("Received OAuth2 Redirect: " + query);  // Print the full response query

        String response = "Login successful! You can close this window.";
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        if (query != null && query.contains("code=")) {
            String code = query.split("code=")[1].split("&")[0];
            authCodeFuture.complete(code);
        }
    }
}
