package com.cli.chat.handlers;

import com.cli.chat.util.LoadingAnimation;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrowserHandler {
    private static final int PORT = 6969;
    private static String authCode = null;
    private static final CountDownLatch latch = new CountDownLatch(1);

    public static String getToken() {
        LoadingAnimation.startLoadingAnimation("Awaiting login");

        try {
            startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        openBrowser();
        try {
            latch.await();
        } catch (InterruptedException e) {
            LoadingAnimation.stopLoadingAnimation();
            throw new RuntimeException(e);
        }
        LoadingAnimation.stopLoadingAnimation();

        return authCode;
    }

    private static void openBrowser() {
        String authUrl = "http://ec2-13-246-228-91.af-south-1.compute.amazonaws.com:8080/oauth2/authorization/google";

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(authUrl));
            } else {
                Runtime.getRuntime().exec("xdg-open " + authUrl); // Linux alternative
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/callback", exchange -> {
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();
            Map<String, String> params = parseQuery(query);
            authCode = params.get("code");

            String response = "Authorization successful! You can close this window.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            latch.countDown();
            server.stop(1);
        });

        new Thread(server::start).start();
    }

    private static Map<String, String> parseQuery(String query) {
        return query == null ? Map.of() :
                Stream.of(query.split("&"))
                        .map(param -> param.split("="))
                        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }
}
