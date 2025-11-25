package com.postarc.postarc_backend.comments.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String username,
        LocalDateTime createdAt) {
}
