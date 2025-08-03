package service;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.Optional;

public class AuthService {
    private final String baseUrl;
    private final HttpClient client;
    private final CookieManager cookieManager;

    public AuthService(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;

        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public void login(String username, String password) throws IOException, InterruptedException {
        HttpRequest getLoginPage = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/accounts/login/"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getLoginPage, HttpResponse.BodyHandlers.ofString());

        if (getResponse.statusCode() != 200) {
            throw new IOException("Failed to load login page, status: " + getResponse.statusCode());
        }

        String csrfToken = getCsrfToken();
        if (csrfToken == null) {
            throw new IOException("CSRF token not found");
        }

        String formData = String.format("username=%s&password=%s&csrfmiddlewaretoken=%s",
                encodeValue(username), encodeValue(password), encodeValue(csrfToken));

        HttpRequest postLogin = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/accounts/login/"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Referer", baseUrl + "/accounts/login/")
                .header("X-CSRFToken", csrfToken)
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> postResponse = client.send(postLogin, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() >= 400) {
            throw new IOException("Login failed, status: " + postResponse.statusCode());
        }

        if (getSessionId() == null) {
            throw new IOException("Session ID not found after login");
        }
    }

    public String getCsrfToken() {
        return getCookieValue("csrftoken");
    }

    public String getSessionId() {
        return getCookieValue("sessionid");
    }

    private String getCookieValue(String name) {
        List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
        Optional<HttpCookie> cookie = cookies.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst();
        return cookie.map(HttpCookie::getValue).orElse(null);
    }

    public HttpClient getClient() {
        return client;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private static String encodeValue(String value) {
        try {
            return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }
}
