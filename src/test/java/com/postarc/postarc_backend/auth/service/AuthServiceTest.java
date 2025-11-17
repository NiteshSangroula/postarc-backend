package com.postarc.postarc_backend.auth.service;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postarc.postarc_backend.auth.dto.RegisterRequest;
import com.postarc.postarc_backend.common.exceptions.DuplicateResourceException;
import com.postarc.postarc_backend.users.dto.UserResponse;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthService authService;

    @Test
    void register_success() {
        RegisterRequest req = new RegisterRequest("nitesh", "nitesh@example.com", "password123", "Nitesh");
        when(userRepository.existsByUsername("nitesh")).thenReturn(false);
        when(userRepository.existsByEmail("nitesh@example.com")).thenReturn(false);
        User savedUser = User.builder().id(1L).username("nitesh").email("nitesh@example.com").displayName("Nitesh")
                .roles(Set.of("ROLE_USER")).build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse resp = authService.register(req);

        assertThat(resp.id()).isEqualTo(1L);
        assertThat(resp.username()).isEqualTo("nitesh");
        assertThat(resp.email()).isEqualTo("nitesh@example.com");
        assertThat(resp.displayName()).isEqualTo("Nitesh");
    }

    @Test
    void register_duplicateUsername() {
        RegisterRequest req = new RegisterRequest("nitesh", "nitesh2@example.com", "password123", "Nitesh");
        when(userRepository.existsByUsername("nitesh")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void register_duplicateEmail() {
        RegisterRequest req = new RegisterRequest("nitesh2", "nitesh@example.com", "password123", "Nitesh");
        when(userRepository.existsByUsername("nitesh2")).thenReturn(false);
        when(userRepository.existsByEmail("nitesh@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already exists");
    }

}
