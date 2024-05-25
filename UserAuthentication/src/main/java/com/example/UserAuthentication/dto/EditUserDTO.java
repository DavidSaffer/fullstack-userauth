package com.example.UserAuthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
