package service;

import enums.CardPack;

import java.net.URI;
import java.net.http.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PackService {
    private final AuthService auth;
    private final Logger logger;

    public PackService(AuthService auth, Logger logger) {
        this.auth = auth;
        this.logger = logger;
    }

    public void buyPackParallel(CardPack pack, int count) {
        int threads = count;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(auth.getBaseUrl() + "/buy-pack/" + pack.getPath()))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("X-CSRFToken", auth.getCsrfToken())
                            .header("Cookie", getCookieHeader())
                            .POST(HttpRequest.BodyPublishers.ofString("&csrfmiddlewaretoken=" + auth.getCsrfToken()))
                            .build();

                    HttpResponse<String> response = auth.getClient().send(request, HttpResponse.BodyHandlers.ofString());
                    logger.info("Buy pack response: " + response.body());
                } catch (Exception e) {
                    logger.warning("Error buying pack: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warning("Pack buying tasks did not finish in time, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warning("Pack buying interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private String getCookieHeader() {
        return "sessionid=" + auth.getSessionId() + "; csrftoken=" + auth.getCsrfToken();
    }
}
