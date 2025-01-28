package com.mpvu.api.v20.controller.oauth2.google;

import com.mpvu.api.v20.dto.oauth2.ConfirmPasswordRequest;
import com.mpvu.api.v20.service.oauth2.google.ConfirmPasswordGoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/oauth2/google")
public class ConfirmPasswordGoogle {

    @Autowired
    ConfirmPasswordGoogleService confirmPasswordService;

    @PostMapping("/confirm-password")
    public ResponseEntity<?> confirmPassword(@RequestBody ConfirmPasswordRequest request) {
        confirmPasswordService.confirmPassword(request);
        return ResponseEntity.ok().build();
    }
}
