package com.example.UserAuthentication.dto;

/**
 * Data Transfer Object for conveying user information.
 * This class is designed to provide a structured form to user data that might be sent to or retrieved from the API.
 * It encapsulates the user's username, email, phone number, and role.
 */
public class UserInfoDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String role;

    /**
     * Constructs a new UserInfoDTO with specified user details.
     *
     * @param username the username of the user.
     * @param email the email address of the user.
     * @param phoneNumber the phone number of the user, can be null if not provided.
     * @param role the role of the user within the system.
     */
    public UserInfoDTO(String username, String email, String phoneNumber, String role) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
