package com.mpvu.api.v20.service.platforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpvu.api.v20.config.TikTokConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TikTokTokenService {
    ObjectMapper objectMapper = new ObjectMapper();

    private final TikTokConfig tikTokConfig;

    @Autowired
    public TikTokTokenService(TikTokConfig tikTokConfig) {
        this.tikTokConfig = tikTokConfig;
    }

    public JsonNode getAccessTokenFromCode(String code) throws IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://open.tiktokapis.com/v2/oauth/token/";

        String body = "code=" + code +
                "&client_key=" + tikTokConfig.getClientKey() +
                "&client_secret=" + tikTokConfig.getClientSecret() +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + tikTokConfig.getRedirectUri();

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

        String url = "https://open-api.tiktok.com/oauth/refresh_token/";

        String body = "refresh_token=" + refreshToken +
                "&client_secret=" + tikTokConfig.getClientSecret() +
                "&client_key=" + tikTokConfig.getClientKey() +
                "&grant_type=refresh_token";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(response.body());
    }
}
