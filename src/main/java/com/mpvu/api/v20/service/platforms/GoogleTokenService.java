package com.mpvu.api.v20.service.platforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoogleTokenService {
    ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getAccessTokenFromCode(String code) throws IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://oauth2.googleapis.com/token";

        String body = "code=" + code +
                "&client_id=512386870403-7kn9sf33r5veed963uto9ubgl3hicdav.apps.googleusercontent.com" +
                "&client_secret=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" +
                "&redirect_uri=http://localhost:8080/api/v2/oauth2/google/callback" +
                "&grant_type=authorization_code";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(response.body());
    }

    public JsonNode getAccessTokenWithRefreshToken(String refreshToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://oauth2.googleapis.com/token";

        String body = "refresh_token=" + refreshToken +
                "&client_id=512386870403-7kn9sf33r5veed963uto9ubgl3hicdav.apps.googleusercontent.com" +
                "&client_secret=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" +
                "&grant_type=refresh_token";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(response.body());
    }

    public JsonNode getUserInfo(String accessToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(response.body());
    }
}