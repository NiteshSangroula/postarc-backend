package com.postarc.postarc_backend.posts.dto;

import java.time.LocalDateTime;

public record PostResponse(
        long id,
        String authorName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
