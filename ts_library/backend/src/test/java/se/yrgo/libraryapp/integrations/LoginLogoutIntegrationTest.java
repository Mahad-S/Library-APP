package se.yrgo.libraryapp.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;
import org.testcontainers.junit.jupiter.Container;
import org.junit.jupiter.api.Test;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

@Tag("integration")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.29");

    @Test
    public void alwaysOkToLogout(int serverPort) throws IOException {
        HttpRequest request = createRequest(serverPort, "/logout")
                .GET()
                .build();

        HttpResponse<String> response = sendRequest(request);

        assertEquals("", response.body());
        assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void testCorrectLoginAndLogout(int serverPort) throws IOException {
        HttpRequest loginRequest = createRequest(serverPort, "/login")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"username\":\"test\",\"password\":\"yrgoP4ssword\"}"))
                .build();

        HttpResponse<String> loginResponse = sendRequest(loginRequest);

        assertEquals(HttpURLConnection.HTTP_OK, loginResponse.statusCode());

        String cookie = loginResponse.headers()
                .firstValue("Set-Cookie")
                .orElseThrow();

        HttpRequest logoutRequest = createRequest(serverPort, "/logout")
                .header("Cookie", cookie.split(";", 2)[0])
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> logoutResponse = sendRequest(logoutRequest);

        assertEquals(HttpURLConnection.HTTP_OK, logoutResponse.statusCode());
    }

    @Test
    void testLoginWithInvalidUser(int serverPort) {
        HttpRequest loginRequest = createRequest(serverPort, "/login")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"username\":\"wronguser\",\"password\":\"wrongpassword\"}"))
                .build();

        HttpResponse<String> response = sendRequest(loginRequest);

        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, response.statusCode());
    }

    private static HttpRequest.Builder createRequest(int serverPort, String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + serverPort + path));
    }

    private static HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return CLIENT.send(request, BodyHandlers.ofString());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            fail(ex);
            throw new IllegalStateException("Request was interrupted", ex);
        } catch (IOException ex) {
            fail(ex);
            throw new IllegalStateException("HTTP request failed", ex);
        }
    }

    public static App createApp() {
        System.setProperty("db.url", MYSQL_CONTAINER.getJdbcUrl());
        System.setProperty("db.user", MYSQL_CONTAINER.getUsername());
        System.setProperty("db.password", MYSQL_CONTAINER.getPassword());

        return new App();
    }

    @AfterAll
    static void reset() {
        System.clearProperty("db.url");
        System.clearProperty("db.user");
        System.clearProperty("db.password");
    }
}
