package com.mpvu.api.v20.controller.user;

import com.mpvu.api.v20.dto.user.LoginRequest;
import com.mpvu.api.v20.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/user")
public class Login {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        loginService.login(request);
        return ResponseEntity.ok().build();
    }
}
