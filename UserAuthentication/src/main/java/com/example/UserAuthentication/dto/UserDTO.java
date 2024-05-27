package com.example.UserAuthentication.dto;

import com.example.UserAuthentication.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

