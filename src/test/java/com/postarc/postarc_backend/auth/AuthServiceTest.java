package com.postarc.postarc_backend.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.postarc.postarc_backend.auth.dto.LoginRequest;
import com.postarc.postarc_backend.auth.dto.RegisterRequest;
import com.postarc.postarc_backend.auth.service.AuthService;
import com.postarc.postarc_backend.security.jwt.JwtService;
import com.postarc.postarc_backend.users.model.User;
import com.postarc.postarc_backend.users.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtService = Mockito.mock(JwtService.class);

        authService = new AuthService(userRepository, passwordEncoder, jwtService);
    }

    // --------------------------------------------
    // TEST: register()
    // --------------------------------------------
    @Test
    void register_ShouldSaveUser() {
        RegisterRequest request = new RegisterRequest("test", "test@mail.com", "pass123", "Test");

        when(passwordEncoder.encode("pass123")).thenReturn("ENCODED");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        var response = authService.register(request);

        assertThat(response).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
    }

    // --------------------------------------------
    // TEST: login() success
    // --------------------------------------------
    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        LoginRequest request = new LoginRequest("test@mail.com", "secret");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("HASHED");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("secret", "HASHED"))
                .thenReturn(true);

        when(jwtService.generateToken(anyMap(), eq("test@mail.com")))
                .thenReturn("FAKE_TOKEN");

        var response = authService.login(request);

        assertThat(response.token()).isEqualTo("FAKE_TOKEN");
    }

    // --------------------------------------------
    // TEST: login() wrong password
    // --------------------------------------------
    @Test
    void login_ShouldThrow_WhenPasswordInvalid() {
        LoginRequest request = new LoginRequest("test@mail.com", "wrong");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("HASHED");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "HASHED"))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    // --------------------------------------------
    // TEST: login() user not found
    // --------------------------------------------
    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        LoginRequest request = new LoginRequest("ghost@mail.com", "anything");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(Exception.class);
    }
}
