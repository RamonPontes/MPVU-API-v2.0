package com.mpvu.api.v20.controller.oauth2.tiktok;

import com.mpvu.api.v20.service.oauth2.tiktok.TikTokCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/oauth2/tiktok")
public class TikTokCallback {

    @Autowired
    TikTokCallbackService tikTokCallbackService;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String userToken, @RequestParam String code) throws IOException, InterruptedException {
        tikTokCallbackService.callback(userToken, code);
        return ResponseEntity.ok().build();
    }
}
