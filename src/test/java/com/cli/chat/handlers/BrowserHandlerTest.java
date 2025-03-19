package com.cli.chat.handlers;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BrowserHandlerTest {

    @Test
    void parseQuery_ShouldReturnCorrectMap() {
        String query = "code=12345&state=xyz";
        Map<String, String> result = BrowserHandler.parseQuery(query);

        assertEquals(2, result.size());
        assertEquals("12345", result.get("code"));
        assertEquals("xyz", result.get("state"));
    }

    @Test
    void parseQuery_ShouldReturnEmptyMapForNull() {
        Map<String, String> result = BrowserHandler.parseQuery(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void startServer_ShouldRespondToRequest() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> {
            try {
                BrowserHandler.startServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

        Thread.sleep(1000); // Give the server time to start

        URL url = new URL("http://localhost:6969/callback?code=test123");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }
}
