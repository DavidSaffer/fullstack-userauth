package com.example.UserAuthentication.model;

import com.example.UserAuthentication.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private UserRoles userRole = UserRoles.EMPLOYEE;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String phoneNumber;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    // getters and setters
}
