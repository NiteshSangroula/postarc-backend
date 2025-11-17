package com.postarc.postarc_backend.users.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String displayName) {
}
