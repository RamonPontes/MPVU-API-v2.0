package com.mpvu.api.v20.dto.video;

import com.google.api.services.youtube.model.Video;

public record VideoUploadRequest(
        String googleVideoData
) { }
