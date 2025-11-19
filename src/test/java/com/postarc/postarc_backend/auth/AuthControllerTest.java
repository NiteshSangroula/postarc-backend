package com.postarc.postarc_backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postarc.postarc_backend.auth.controller.AuthController;
import com.postarc.postarc_backend.auth.dto.AuthResponse;
import com.postarc.postarc_backend.auth.dto.LoginRequest;
import com.postarc.postarc_backend.auth.dto.RegisterRequest;
import com.postarc.postarc_backend.auth.service.AuthService;
import com.postarc.postarc_backend.users.dto.UserResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_ShouldReturnOk() {
        RegisterRequest req = new RegisterRequest("test", "test@mail.com", "password123", "TestUser");

        UserResponse resp = new UserResponse(
                1L,
                "test@mail.com",
                "TestUser",
                "Test");

        when(authService.register(req)).thenReturn(resp);

        ResponseEntity<UserResponse> response = authController.register(req);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(resp.email(), response.getBody().email());
        assertEquals(resp.displayName(), response.getBody().displayName());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenInvalidData() {
        RegisterRequest req = new RegisterRequest("", "", "", "");

        when(authService.register(req)).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () -> authController.register(req));
    }

    @Test
    void login_ShouldReturnToken() {
        LoginRequest req = new LoginRequest("test@mail.com", "password123");

        String token = "mocked.jwt.token";
        when(authService.login(req)).thenReturn(new AuthResponse(token));

        ResponseEntity<AuthResponse> response = authController.login(req);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(token, response.getBody().token());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenWrongPassword() {
        LoginRequest req = new LoginRequest("test@mail.com", "wrongpass");

        when(authService.login(req)).thenThrow(new IllegalArgumentException("Invalid credentials"));

        assertThrows(IllegalArgumentException.class, () -> authController.login(req));
    }
}
