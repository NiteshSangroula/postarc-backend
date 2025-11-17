package com.postarc.postarc_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterRequest
 */

public record RegisterRequest(
        @NotBlank String username,
        @Email String email,
        @NotBlank @Size(min = 6) String password,
        String displayName) {
}
