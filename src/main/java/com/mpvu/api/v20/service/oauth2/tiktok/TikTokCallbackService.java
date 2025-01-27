package com.mpvu.api.v20.service.oauth2.tiktok;

import com.fasterxml.jackson.databind.JsonNode;
import com.mpvu.api.v20.model.User;
import com.mpvu.api.v20.model.UserTokens;
import com.mpvu.api.v20.repository.UserRepository;
import com.mpvu.api.v20.repository.UserTokensRepository;
import com.mpvu.api.v20.service.platforms.TikTokTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
public class TikTokCallbackService {

    @Autowired
    TikTokTokenService tikTokTokenService;

    @Autowired
    JwtDecoder jwtDecoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTokensRepository userTokensRepository;

    String redirectUrl = "https://4275-2804-d55-4806-1100-30e1-4ac4-aade-4ef8.ngrok-free.app/connect-account/TIKTOK";

    public void callback(String userToken, String code) throws IOException, InterruptedException {
        if (userToken == null || code == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User token and code are required");
        }

        JsonNode jsonNode = tikTokTokenService.getAccessTokenFromCode(code, redirectUrl);

        if (!jsonNode.has("refresh_token")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
        }

        String refreshToken = jsonNode.get("refresh_token").toString().replace("\"", "");

        String userId;

        try {
            Jwt decodedJwt = jwtDecoder.decode(userToken);
            userId = decodedJwt.getClaim("sub");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user token");
        }

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserTokens userTokens = new UserTokens();

        userTokens.setToken(refreshToken);
        userTokens.setUserId(user.getUserId());

        userTokensRepository.save(userTokens);
    }
}
