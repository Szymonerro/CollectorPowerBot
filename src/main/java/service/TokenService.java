package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.CollectResponse;
import model.TokenStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TokenService {
    private final AuthService auth;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger;

    public TokenService(AuthService auth, Logger logger) {
        this.auth = auth;
        this.logger = logger;
    }

    public TokenStatus checkStatus() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(auth.getBaseUrl() + "/api/token-status/"))
                .header("Cookie", getCookieHeader())
                .GET()
                .build();

        HttpResponse<String> response = auth.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), TokenStatus.class);
    }

    public void collectTokensParallel(int count) {
        int threads = count;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(auth.getBaseUrl() + "/api/collect-token/"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("X-CSRFToken", auth.getCsrfToken())
                            .header("Cookie", getCookieHeader())
                            .POST(HttpRequest.BodyPublishers.ofString("csrfmiddlewaretoken=" + auth.getCsrfToken()))
                            .build();

                    HttpResponse<String> response = auth.getClient().send(request, HttpResponse.BodyHandlers.ofString());
                    CollectResponse result = mapper.readValue(response.body(), CollectResponse.class);
                    logger.info("Collect token response: " + response.body());
                } catch (Exception e) {
                    logger.warning("Error collecting token: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                logger.warning("Token collection tasks did not finish in time, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warning("Token collection interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private String getCookieHeader() {
        return "sessionid=" + auth.getSessionId() + "; csrftoken=" + auth.getCsrfToken();
    }
}
