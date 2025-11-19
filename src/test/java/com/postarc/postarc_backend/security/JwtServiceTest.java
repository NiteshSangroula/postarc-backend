package com.postarc.postarc_backend.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.postarc.postarc_backend.security.jwt.JwtService;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret",
                "this_is_a_super_long_secret_key_that_is_valid_1234567890");

        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000);

        jwtService.init();
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtService.generateToken(
                Map.of("userId", 1L),
                "test@example.com");

        assertThat(jwtService.isTokenValid(token)).isTrue();

        Claims claims = jwtService.extractAllClaims(token);

        assertThat(claims.get("userId", Integer.class)).isEqualTo(1);
        assertThat(claims.getSubject()).isEqualTo("test@example.com");
    }
}
