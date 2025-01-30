package com.mpvu.api.v20.service.video.platforms;

import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.mpvu.api.v20.dto.video.VideoUploadRequest;
import com.mpvu.api.v20.model.UserTokens;
import com.mpvu.api.v20.repository.UserTokensRepository;
import com.mpvu.api.v20.service.platforms.GoogleTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class GoogleVideoUpload {
    @Autowired
    UserTokensRepository userTokensRepository;

    @Autowired
    GoogleTokenService googleTokenService;

    public String upload(VideoUploadRequest videoData, UUID userId, MultipartFile video) throws Exception {
        UserTokens token = userTokensRepository.findByUserIdAndPlatform(userId, "google")
                .orElseThrow(() -> new Exception("User is not logged in"));

        String accessTokenValue = googleTokenService.getAccessTokenWithRefreshToken(token.getToken()).get("access_token").toString().replace("\"", "");

        if (accessTokenValue.isEmpty()) {
            throw new Exception("Access token not found");
        }

        AccessToken accessToken = new AccessToken(accessTokenValue, new Date(System.currentTimeMillis() + 3600 * 1000));
        GoogleCredentials googleCredentials = GoogleCredentials.create(accessToken);

        YouTube youTube = new YouTube.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(googleCredentials)
        ).setApplicationName("MPVU").build();

        InputStreamContent mediaContent = createVideoInsertMediaContent(video);

        YouTube.Videos.Insert request = youTube.videos().insert("snippet, status", createVideoObject(), mediaContent);

        Video response = request.execute();

        return "https://www.youtube.com/watch?v=" + response.getId();
    }

    private Video createVideoObject() {
        Video video = new Video();

        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle("title");
        snippet.setDescription("description");

        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public");

        video.setSnippet(snippet);
        video.setStatus(status);
        return video;
    }

    private InputStreamContent createVideoInsertMediaContent(MultipartFile video) throws IOException {
        File tempFile = File.createTempFile("video", ".mp4");
        video.transferTo(tempFile);
        FileInputStream fileInputStream = new FileInputStream(tempFile);

        return new InputStreamContent("video/mp4", fileInputStream);
    }
}
