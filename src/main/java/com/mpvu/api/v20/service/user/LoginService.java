package com.mpvu.api.v20.service.user;

import com.mpvu.api.v20.dto.user.LoginRequest;
import com.mpvu.api.v20.integration.VerifyEmail;
import com.mpvu.api.v20.model.User;
import com.mpvu.api.v20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class LoginService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtEncoder jwtEncoder;

    public void login(LoginRequest loginRequest) {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid username"));

        if (!user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        var now = Instant.now();
        var expiresIn = 60 * 60 * 24 * 30L;

        var claims = JwtClaimsSet.builder()
                .issuer("MpvuAPIv2.0")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        System.out.println(jwtValue);
    }
}
