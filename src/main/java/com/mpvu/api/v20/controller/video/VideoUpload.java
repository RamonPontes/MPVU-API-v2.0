package com.mpvu.api.v20.controller.video;

import com.mpvu.api.v20.dto.video.VideoUploadRequest;
import com.mpvu.api.v20.service.video.VideoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/video")
public class VideoUpload {
    @Autowired
    VideoUploadService videoUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("platforms") String platformsString,
            @RequestPart("data") VideoUploadRequest videoData,
            @RequestPart("video") MultipartFile video,
            JwtAuthenticationToken token
    ) {
        return ResponseEntity.ok(videoUploadService.upload(token, platformsString, videoData, video));
    }
}
