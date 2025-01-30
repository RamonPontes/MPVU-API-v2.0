package com.mpvu.api.v20.service.video.platforms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpvu.api.v20.dto.video.VideoUploadRequest;
import com.mpvu.api.v20.model.UserTokens;
import com.mpvu.api.v20.repository.UserTokensRepository;
import com.mpvu.api.v20.service.platforms.TikTokTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class TikTokVideoUpload {
    long DEFAULT_CHUNK_SIZE = 67108864; // 64MB

    @Autowired
    TikTokTokenService tikTokTokenService;

    @Autowired
    UserTokensRepository userTokensRepository;

    public String upload(VideoUploadRequest videoData, UUID userId, MultipartFile video) throws Exception {
        UserTokens userTokens = userTokensRepository.findByUserIdAndPlatform(userId, "tiktok")
                .orElseThrow(() -> new Exception("Token not found"));

        String accessTokenValue = tikTokTokenService.getAccessTokenWithRefreshToken(userTokens.getToken()).path("data").get("access_token").toString().replace("\"", "");

        if (accessTokenValue.isEmpty()) {
            throw new Exception("Access token not found");
        }

        long video_size = video.getSize();

        long chunk_size = Math.min(DEFAULT_CHUNK_SIZE, video_size);
        long total_chunk_count = (long) Math.ceil((double) video_size / chunk_size);

        HttpClient client = HttpClient.newHttpClient();
        String url = "https://open.tiktokapis.com/v2/post/publish/inbox/video/init/";

        String body = """
                {
                    "source_info": {
                        "source": "FILE_UPLOAD",
                        "video_size": %d,
                        "chunk_size": %d,
                        "total_chunk_count": %d
                    }
                }
                """.formatted(video_size, chunk_size, total_chunk_count);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Bearer " + accessTokenValue)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonResponse = objectMapper.readTree(response.body());

        System.out.println(jsonResponse.toString());

        return "ok";
    }
}
