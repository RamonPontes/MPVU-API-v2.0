// https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=512386870403-7kn9sf33r5veed963uto9ubgl3hicdav.apps.googleusercontent.com&redirect_uri=http://localhost:8080/api/v2/oauth2/google/callback&scope=email%20profile&access_type=offline&prompt=consent

package com.mpvu.api.v20.controller.oauth2.google;

import com.mpvu.api.v20.service.oauth2.google.GoogleCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/oauth2/google")
public class GoogleCallback {

    @Autowired
    GoogleCallbackService googleCallbackService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code) throws IOException, InterruptedException {
        return ResponseEntity.ok(googleCallbackService.callback(code));
    }
}
