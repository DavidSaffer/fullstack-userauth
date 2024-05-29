package com.example.UserAuthentication.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user login requests.
 * This class is used to capture and transfer user credentials for authentication purposes.
 *
 * Fields:
 * - {@code username}: The username of the user attempting to log in. Cannot be blank.
 * - {@code password}: The password of the user. Cannot be blank.
 *
 */
public class LoginDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;

    // Constructors, getters, and setters
    public LoginDTO() {}

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

