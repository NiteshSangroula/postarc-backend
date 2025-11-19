package com.postarc.postarc_backend.auth.dto;

public record LoginRequest(
        String email,
        String password) {
}
