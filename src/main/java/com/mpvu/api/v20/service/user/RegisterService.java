package com.mpvu.api.v20.service.user;

import com.mpvu.api.v20.dto.user.RegisterRequest;
import com.mpvu.api.v20.integration.VerifyEmail;
import com.mpvu.api.v20.model.User;
import com.mpvu.api.v20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegisterService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VerifyEmail verifyEmail;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(RegisterRequest registerRequest) {
        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password and email are required");
        }

        if (!verifyEmail.verify(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }

        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
                });

        User user = new User();

        user.setEmail(registerRequest.email());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.password()));
        user.setUsername(registerRequest.username());

        userRepository.save(user);
    }
}
