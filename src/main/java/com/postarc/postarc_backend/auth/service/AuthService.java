package com.postarc.postarc_backend.auth.service;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.postarc.postarc_backend.auth.dto.RegisterRequest;
import com.postarc.postarc_backend.common.exceptions.DuplicateResourceException;
import com.postarc.postarc_backend.users.dto.UserResponse;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .displayName(request.displayName())
                .roles(Set.of("ROLE_USER"))
                .build();

        user = userRepository.save(user);

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getDisplayName());
    }

}
