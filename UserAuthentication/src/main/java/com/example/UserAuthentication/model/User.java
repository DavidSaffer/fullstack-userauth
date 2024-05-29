package com.example.UserAuthentication.model;

import com.example.UserAuthentication.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a user entity in the system database.
 * This class is annotated with JPA annotations to map it to the "users" table in the database.
 * It includes fields for user details such as username, password, roles, and contact information.
 *
 * Fields:
 * - {@code userId}: Primary key of the user, generated automatically.
 * - {@code username}: User's username; must not be blank and cannot contain only whitespace.
 * - {@code password}: User's password; must not be blank.
 * - {@code role}: User's role in the system, defaults to USER. Managed via enum {@link UserRoles}.
 * - {@code email}: User's email address.
 * - {@code phoneNumber}: User's phone number.
 * - {@code dateCreated}: Timestamp when the user was created.
 * - {@code dateUpdated}: Timestamp when the user was last updated, automatically updated on modification.
 *
 * Logbook's annotations are used to minimize boilerplate code for getter, setter, equals, hashcode, and toString methods.
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long userId;

    @NotBlank(message = "Username must not be empty") // Ensures it's not null and not just whitespace
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    @EqualsAndHashCode.Include
    private UserRoles role = UserRoles.USER;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String phoneNumber;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    // Method to automatically update the updatedAt timestamp before any update operation
    @PreUpdate
    protected void onUpdate() {
        dateUpdated = new Timestamp(new Date().getTime()).toLocalDateTime();
    }


    // getters and setters

    /**
     * Custom setter for username to trim leading and trailing whitespace.
     * @param username the username to set, whitespace will be trimmed.
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * Overloaded setter for role that takes a {@link UserRoles} as parameter.
     * @param role the user role to set.
     */
    public void setRole(UserRoles role) {
        this.role = role;
    }

    /**
     * Overloaded setter for role that takes a string as parameter and converts it to {@link UserRoles} enum.
     * @param role the string representation of the user role.
     * @throws IllegalArgumentException if the string does not match any {@link UserRoles}.
     */
    public void setRole(String role) throws IllegalArgumentException {
        try {
            this.role = UserRoles.valueOf(role.toUpperCase()); // Convert string to UserRole enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: '" + role + "'. Please use one of the following values: " + Arrays.toString(UserRoles.values()));
        }
    }
}
