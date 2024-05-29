package com.example.UserAuthentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Data Transfer Object for capturing user signup details.
 * This class is used for user registration processes and ensures that all necessary information
 * is provided and correctly formatted. The class includes the following fields:
 *
 * - {@code username}: The username for the new user account. It must not be blank.
 * - {@code password}: The password for the new user account. It must not be blank.
 * - {@code email}: The email address for the new user account. It must be a valid email format.
 * - {@code phoneNumber}: Optional phone number for the new user account.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignupDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    // Constructors, getters, and setters

}
