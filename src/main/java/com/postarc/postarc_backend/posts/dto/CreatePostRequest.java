package com.postarc.postarc_backend.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank @Size(min = 1, max = 500) String content) {
}
