package com.postarc.postarc_backend.profile;

public record ProfileResponseDTO(
        long userId,
        String profilePictureUrl,
        String userName) {
}
