package com.mpvu.api.v20.controller.user;

import com.mpvu.api.v20.dto.user.RegisterRequest;
import com.mpvu.api.v20.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/user")
public class Register {
    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        registerService.register(request);
        return ResponseEntity.ok().build();
    }
}
