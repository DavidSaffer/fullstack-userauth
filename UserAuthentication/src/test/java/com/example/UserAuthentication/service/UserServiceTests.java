package com.example.UserAuthentication.service;

import com.example.UserAuthentication.dto.LoginResponse;
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

    // Add more tests for other scenarios
}
