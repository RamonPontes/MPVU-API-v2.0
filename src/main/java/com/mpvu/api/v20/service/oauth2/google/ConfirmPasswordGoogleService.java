package com.mpvu.api.v20.service.oauth2.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.mpvu.api.v20.dto.oauth2.ConfirmPasswordRequest;
import com.mpvu.api.v20.model.ConfirmPasswordToken;
import com.mpvu.api.v20.model.User;
import com.mpvu.api.v20.model.UserTokens;
import com.mpvu.api.v20.repository.ConfirmPasswordTokenRepository;
import com.mpvu.api.v20.repository.UserRepository;
import com.mpvu.api.v20.repository.UserTokensRepository;
import com.mpvu.api.v20.service.platforms.GoogleTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConfirmPasswordGoogleService {
    @Autowired
    JwtDecoder jwtDecoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTokensRepository userTokensRepository;

    @Autowired
    GoogleTokenService googleTokenService;

    @Autowired
    ConfirmPasswordTokenRepository confirmPasswordTokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public void confirmPassword(ConfirmPasswordRequest confirmPasswordRequest) {
        if (confirmPasswordRequest.password() == null || confirmPasswordRequest.token() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and token are required");
        }

        try {
            Jwt jwt = jwtDecoder.decode(confirmPasswordRequest.token());
            String refreshToken = jwt.getSubject();

            ConfirmPasswordToken confirmPasswordToken = confirmPasswordTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid"));

            if (confirmPasswordToken.isUsed()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token already used");
            }

            String access_token = googleTokenService.getAccessTokenWithRefreshToken(refreshToken).get("access_token").toString().replace("\"", "");
            JsonNode userInfo = googleTokenService.getUserInfo(access_token);

            User user = new User();

            user.setPassword(bCryptPasswordEncoder.encode(confirmPasswordRequest.password()));
            user.setUsername(userInfo.get("name").toString().replace("\"",""));
            user.setEmail(userInfo.get("email").toString().replace("\"",""));

            userRepository.save(user);

            UserTokens userTokens = new UserTokens();

            userTokens.setUserId(user.getUserId());
            userTokens.setToken(refreshToken);
            userTokens.setPlatform("google");

            userTokensRepository.save(userTokens);

            confirmPasswordToken.setUsed(true);
            confirmPasswordTokenRepository.save(confirmPasswordToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid");
        }
    }
}
