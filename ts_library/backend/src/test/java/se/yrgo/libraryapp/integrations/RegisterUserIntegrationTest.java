
package se.yrgo.libraryapp.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;

@Tag("integration")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class RegisterUserIntegrationTest {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.29");

    @Test
    void testRegisterUser(int serverPort) {
        String username = "test" + UUID.randomUUID();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + serverPort + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"name\":\"" + username
                                + "\",\"realName\":\"Test User\",\"password\":\"password123\"}"))
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, BodyHandlers.ofString());

            assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
            assertEquals("true", response.body());

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            fail(ex);
        } catch (java.io.IOException ex) {
            fail(ex);
        }
    }

    @Test
    void testRegisterExistingUser(int serverPort) throws java.io.IOException {
        String username = "test" + UUID.randomUUID();

        registerUser(serverPort, username);
        HttpResponse<String> response = registerUser(serverPort, username);

        assertEquals("false", response.body());
    }

    private HttpResponse<String> registerUser(int serverPort, String username)
            throws java.io.IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + serverPort + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"name\":\"" + username
                                + "\",\"realName\":\"Test User\",\"password\":\"password123\"}"))
                .build();

        try {
            return CLIENT.send(request, BodyHandlers.ofString());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            fail(ex);
            throw new IllegalStateException(ex);
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
