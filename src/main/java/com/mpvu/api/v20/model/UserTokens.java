package com.mpvu.api.v20.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "t-users-tokens")
public class UserTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;
    private UUID userId;
    private String platform;
    private String token;

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatform() { return platform; }

    public void setPlatform(String platform) { this.platform = platform; }
}
