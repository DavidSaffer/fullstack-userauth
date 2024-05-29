package com.example.UserAuthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for editing user details.
 * This class is used to carry data needed to update user information in the system.
 *
 * Fields:
 * - {@code oldUsername}: The current username of the user to be edited.
 * - {@code newUsername}: The new username for the user, if it needs to be updated.
 * - {@code email}: The new email address for the user.
 * - {@code phoneNumber}: The new phone number for the user.
 * - {@code role}: The role assigned to the user, which can control access and permissions.
 */
@AllArgsConstructor
@Getter
@Setter
public class EditUserDTO {
    private String oldUsername;
    private String newUsername;
    private String email;
    private String phoneNumber;
    private String role;
}
