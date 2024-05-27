package com.example.UserAuthentication.model;

import com.example.UserAuthentication.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;

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

    // Custom setter for username to trim leading and trailing whitespace
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    // Overloaded setter for role that takes UserRole as parameter
    public void setRole(UserRoles role) {
        this.role = role;
    }

    // Overloaded setter for role that takes String as parameter
    public void setRole(String role) throws IllegalArgumentException {
        try {
            this.role = UserRoles.valueOf(role.toUpperCase()); // Convert string to UserRole enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: '" + role + "'. Please use one of the following values: " + Arrays.toString(UserRoles.values()));
        }
    }
}
