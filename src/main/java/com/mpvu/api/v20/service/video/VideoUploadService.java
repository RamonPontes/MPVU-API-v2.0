package com.mpvu.api.v20.service.video;

import com.mpvu.api.v20.dto.video.VideoUploadRequest;
import com.mpvu.api.v20.service.video.platforms.GoogleVideoUpload;
import com.mpvu.api.v20.service.video.platforms.TikTokVideoUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VideoUploadService {
    @Autowired
    GoogleVideoUpload googleVideoUpload;

    @Autowired
    TikTokVideoUpload tikTokVideoUpload;

    public Map<String, String> upload(JwtAuthenticationToken token, String platformsString, VideoUploadRequest videoData, MultipartFile video) {
        if(token == null || !token.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user token");
        }

        List<String> platforms = List.of(platformsString.split(","));

        Map<String, String> response = new HashMap<>();

        for (String platform : platforms) {
            if (platform.equals("tiktok")) {
                try {
                    response.put(platform, tikTokVideoUpload.upload(videoData, UUID.fromString(token.getName()), video));
                } catch (Exception e) {
                    response.put(platform, e.getMessage());
                }
            } else if (platform.equals("google")) {
                try {
                    response.put(platform, googleVideoUpload.upload(videoData, UUID.fromString(token.getName()), video));
                } catch (Exception e) {
                    response.put(platform, e.getMessage());
                }
            } else {
                response.put(platform, "Invalid platform");
            }
        }

        return response;
    }
}