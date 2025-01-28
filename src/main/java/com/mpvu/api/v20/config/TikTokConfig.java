package com.mpvu.api.v20.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikTokConfig {

    @Value("${tiktok.client_key}")
    private String clientKey;

    @Value("${tiktok.client_secret}")
    private String clientSecret;

    @Value("${tiktok.redirect_uri}")
    private String redirectUri;

    public String getClientKey() {
        return clientKey;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
