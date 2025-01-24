package com.mpvu.api.v20.service.oauth2.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.mpvu.api.v20.model.ConfirmPasswordToken;
import com.mpvu.api.v20.repository.ConfirmPasswordTokenRepository;
import com.mpvu.api.v20.service.platforms.GoogleTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;

@Service
public class GoogleCallbackService {
    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    GoogleTokenService googleTokenService;

    @Autowired
    ConfirmPasswordTokenRepository confirmPasswordTokenRepository;

    public String callback(String code) throws IOException, InterruptedException {
        if (code == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code are required");
        }

        JsonNode jsonNode = googleTokenService.getAccessTokenFromCode(code);

        if (!jsonNode.has("refresh_token")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
        }

        String refreshToken = jsonNode.get("refresh_token").toString().replace("\"", "");

        var now = Instant.now();
        var expiresIn = 60 * 10L; // 10m

        var claims = JwtClaimsSet.builder()
                .issuer("MpvuAPIv2.0")
                .subject(refreshToken)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();


        ConfirmPasswordToken confirmPasswordToken = new ConfirmPasswordToken();
        confirmPasswordToken.setToken(refreshToken);
        confirmPasswordToken.setCreatedAt(now);
        confirmPasswordToken.setExpiresAt(now.plusSeconds(expiresIn));

        confirmPasswordTokenRepository.save(confirmPasswordToken);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
