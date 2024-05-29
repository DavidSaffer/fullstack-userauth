package com.example.UserAuthentication.dto;

import com.example.UserAuthentication.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the user data.
 * This class is used to transfer user data between processes, encapsulating details such as
 * user credentials, roles, and timestamps for creation and updates.
 *
 * Fields:
 * - {@code userId}: The unique identifier for the user.
 * - {@code username}: The username of the user. It cannot be blank and is validated accordingly.
 * - {@code password}: The user's password. This field is not subject to a not blank constraint here but should be securely handled.
 * - {@code role}: The role of the user, defined by an enum {@link UserRoles}.
 * - {@code email}: The email address of the user.
 * - {@code phoneNumber}: An optional phone number for the user.
 * - {@code dateCreated}: The timestamp when the user account was created.
 * - {@code dateUpdated}: The timestamp when the user account was last updated.
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private UserRoles role;  // Enum type
    private String email;
    private String phoneNumber;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public UserDTO(Long userId,
                   @NotBlank(message = "Username must not be empty") String username,
                   String password,
                   UserRoles role,
                   String email,
                   String phoneNumber,
                   LocalDateTime dateCreated,
                   LocalDateTime dateUpdated) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }
}

