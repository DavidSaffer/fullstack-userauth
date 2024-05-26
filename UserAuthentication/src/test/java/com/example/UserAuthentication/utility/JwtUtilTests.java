package com.example.UserAuthentication.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * Tests for the JwtUtil class.
 */
public class JwtUtilTests {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        // Initialize JwtUtil with a fixed secret key for testing.
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username, "USER");
        assertNotNull(token, "Token should not be null");

        // Decode the token to verify if it contains the correct username in the subject claim
        String decodedUsername = JWT.decode(token).getSubject();
        assertEquals(username, decodedUsername, "The decoded username should match the original");
    }

    @Test
    public void testGetUsernameFromToken() {
        String expectedUsername = "testUser";
        String token = jwtUtil.generateToken(expectedUsername, "USER");
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals(expectedUsername, username, "Extracted username should match the expected username");
    }

    @Test
    public void testIsTokenExpired() {
        String token = jwtUtil.generateToken("testUser", "USER");

        // Immediately check if the token is expired
        assertFalse(jwtUtil.isTokenExpired(token), "Token should not be expired right after creation");

        // Manipulate the expiration time to simulate an expired token
        String expiredToken = JWT.create()
                .withSubject("testUser")
                .withExpiresAt(new Date(System.currentTimeMillis() - 1000)) // Token expired 1 second ago
                .sign(Algorithm.HMAC256("secret"));
        assertTrue(jwtUtil.isTokenExpired(expiredToken), "Token should be expired");
    }

    @Test
    public void testValidateToken() {
        String username = "validUser";
        String token = jwtUtil.generateToken(username, "USER");

        // Validate token
        assertTrue(jwtUtil.validateToken(token), "Token should be valid");

        // Test with an invalid token
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpbnZhbGlkVXNlciJ9.XYZ";
        assertFalse(jwtUtil.validateToken(invalidToken), "Token should be invalid");
    }
}
