package com.example.UserAuthentication.service;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.enums.UserRoles;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.repository.UserRepository;
import com.example.UserAuthentication.utility.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterNewUser_Success() {
        String username = "testuser";
        String password = "password";
        String email = "testuser@example.com";
        String phoneNumber = "1234567890";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(username, user.getRole().toString())).thenReturn("jwtToken");

        LoginResponse response = userService.registerNewUser(username, password, email, phoneNumber);

        assertTrue(response.isSuccess());
        assertNotNull(response.getToken());
        assertEquals("Registration successful", response.getMessage());

        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtUtil, times(1)).generateToken(username, user.getRole().toString());
    }

    @Test
    void whenRegisterNewUserWithExistingUsername_thenFail() {
        String username = "existingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        LoginResponse response = userService.registerNewUser(username, "pass123", "user@example.com", "1234567890");

        assertFalse(response.isSuccess());
        assertEquals("Username: existingUser is already taken", response.getMessage());
    }

    @Test
    void whenRegisterNewUserWithValidData_thenSucceed() {
        String username = "newUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        LoginResponse response = userService.registerNewUser(username, "pass123", "user@example.com", "1234567890");

        assertTrue(response.isSuccess());
        assertEquals("Registration successful", response.getMessage());
        assertEquals("token", response.getToken());
    }

    @Test
    void whenLoginWithIncorrectPassword_thenFail() {
        String username = "user";
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        LoginResponse response = userService.login(username, "wrongPassword");

        assertFalse(response.isSuccess());
        assertEquals("Incorrect password", response.getMessage());
    }

    @Test
    void whenLoginWithCorrectPassword_thenSucceed() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setRole(UserRoles.USER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(username, user.getRole().toString())).thenReturn("token");

        LoginResponse response = userService.login(username, "correctPassword");

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("token", response.getToken());
    }

    // Edit User
    @Test
    void whenEditUserButUserNotFound_thenFail() {
        String oldUsername = "nonexistent";
        when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.empty());

        ApiResponse<?> response = userService.editUser(oldUsername, "newUser", "new@example.com", "1234567890", "USER", "admin", "ADMIN");

        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void whenEditUserButNotAuthorized_thenFail() {
        String oldUsername = "user";
        User user = new User();
        user.setUsername(oldUsername);
        when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.of(user));

        ApiResponse<?> response = userService.editUser(oldUsername, "newUser", "new@example.com", "1234567890", "USER", "otherUser", "USER");

        assertFalse(response.isSuccess());
        assertEquals("Invalid Permissions", response.getMessage());
    }

    @Test
    void whenEditUserWithValidPermissions_thenSucceed() {
        String oldUsername = "user";
        User user = new User();
        user.setUsername(oldUsername);
        user.setRole(UserRoles.USER);
        when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        ApiResponse<?> response = userService.editUser(oldUsername, "newUser", "new@example.com", "1234567890", "USER", "user", "USER");

        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
    }

    // Delete user
    @Test
    void whenDeleteUserButUserNotFound_thenFail() {
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ApiResponse<?> response = userService.deleteUser(username, "admin", "ADMIN");

        assertFalse(response.isSuccess());
        assertEquals("User " + username + " not found", response.getMessage());
    }

    @Test
    void whenDeleteUserButNotAuthorized_thenFail() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ApiResponse<?> response = userService.deleteUser(username, "other_user", "USER");

        assertFalse(response.isSuccess());
        assertEquals("Invalid Permissions in JWT token", response.getMessage());
    }

    @Test
    void whenDeleteUserWithPermission_thenSucceed() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ApiResponse<?> response = userService.deleteUser(username, "admin", "ADMIN");

        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
    }
}
