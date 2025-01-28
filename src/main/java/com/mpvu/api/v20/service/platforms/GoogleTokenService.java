package com.mpvu.api.v20.service.platforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpvu.api.v20.config.GoogleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoogleTokenService {
    ObjectMapper objectMapper = new ObjectMapper();

    private final GoogleConfig googleConfig;

    @Autowired
    public GoogleTokenService(GoogleConfig googleConfig) {
        this.googleConfig = googleConfig;
    }

    public JsonNode getAccessTokenFromCode(String code) throws IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://oauth2.googleapis.com/token";

        String body = "code=" + code +
                "&client_id=" + googleConfig.getClientId() +
                "&client_secret=" + googleConfig.getClientSecret()  +
                "&redirect_uri=" + googleConfig.getRedirectUri()  +
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
                "&client_id=" + googleConfig.getClientId()  +
                "&client_secret=" + googleConfig.getClientSecret()  +
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