package com.example.UserAuthentication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the response structure for login attempts.
 * This class encapsulates the outcome of the authentication process, including success status, authentication token, and a message.
 *
 * Annotations:
 * - {@code @Getter}: Automatically generates getters for all fields.
 * - {@code @Setter}: Automatically generates setters for all fields.
 * - {@code @ToString}: Automatically generates a toString method that includes all fields.
 */
@Getter
@Setter
@ToString
public class LoginResponse {
    private boolean success;
    private String token;
    private String message;

    /**
     * Constructs a new LoginResponse with the specified success status, token, and message.
     *
     * @param success a boolean indicating if the login was successful.
     * @param token the authentication token issued on successful login.
     * @param message a descriptive message about the login attempt.
     */
    public LoginResponse(boolean success, String token, String message) {
        this.success = success;
        this.token = token;
        this.message = message;
    }

    // Getters and setters

}

