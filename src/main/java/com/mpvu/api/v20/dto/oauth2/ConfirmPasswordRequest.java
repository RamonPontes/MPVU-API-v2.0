package com.mpvu.api.v20.dto.oauth2;

public record ConfirmPasswordRequest(String token, String password) {
}
