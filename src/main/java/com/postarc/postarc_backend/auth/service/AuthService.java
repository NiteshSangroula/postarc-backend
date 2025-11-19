package com.postarc.postarc_backend.auth.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.postarc.postarc_backend.auth.dto.AuthResponse;
import com.postarc.postarc_backend.auth.dto.LoginRequest;
import com.postarc.postarc_backend.auth.dto.RegisterRequest;
import com.postarc.postarc_backend.common.exceptions.DuplicateResourceException;
import com.postarc.postarc_backend.security.jwt.JwtService;
import com.postarc.postarc_backend.users.dto.UserResponse;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(Map.of("userId", user.getId()), user.getEmail());

        return new AuthResponse(token);

    }

}
