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
public class TikTokTokenService {
    ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getAccessTokenFromCode(String code, String redirect_uri) throws IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();

        String url = "https://open.tiktokapis.com/v2/oauth/token/";

        String body = "code=" + code +
                "&client_key=sbaw2ph3axn2dbbr1p" +
                "&client_secret=QMKPYyJC1h5tr3nydAdOpAiNhhMIlglZ" +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + redirect_uri;

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
                "&client_secret=QMKPYyJC1h5tr3nydAdOpAiNhhMIlglZ" +
                "&client_key=sbaw2ph3axn2dbbr1p" +
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
