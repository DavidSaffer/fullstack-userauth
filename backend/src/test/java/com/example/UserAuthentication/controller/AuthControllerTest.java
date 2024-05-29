package com.example.UserAuthentication.controller;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.LoginDTO;
import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.dto.SignupDTO;
import com.example.UserAuthentication.service.UserService;
import com.example.UserAuthentication.utility.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new MockHttpServletResponse();
    }

    @Test
    void whenSignUp_thenRegisterNewUser() {
        SignupDTO signupDTO = new SignupDTO("newUser", "password123", "new@example.com", "1234567890");
        when(userService.registerNewUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new LoginResponse(true, "token", "User registered successfully"));

        ResponseEntity<?> result = authController.signup(signupDTO, response);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(response.getCookie("jwt"));
    }

    @Test
    void whenLogin_thenAuthenticate() {
        LoginDTO loginDTO = new LoginDTO("user", "password");
        when(userService.login(anyString(), anyString()))
                .thenReturn(new LoginResponse(true, "token", "Login successful"));

        ResponseEntity<?> result = authController.login(loginDTO, response);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(response.getCookie("jwt"));
    }

    @Test
    void whenLogout_thenLogoutUser() {
        ResponseEntity<?> result = authController.logout(response);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Logged out successfully", result.getBody());
        assertEquals(0, Objects.requireNonNull(response.getCookie("jwt")).getMaxAge());
    }

    @Test
    void whenDeleteUser_thenDeleteUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("jwt", "token"));
        when(jwtUtil.getUsernameFromToken("token")).thenReturn("admin");
        when(jwtUtil.getRoleFromToken("token")).thenReturn("ADMIN");
        ApiResponse<Boolean> result2 = new ApiResponse<Boolean>(true, "User deleted successfully", false);
        when(userService.deleteUser(anyString(), anyString(), anyString()))
                .thenReturn((ApiResponse) new ApiResponse<>(true, "User deleted successfully", Boolean.FALSE));


        ResponseEntity<?> result = authController.deleteUser("user", request, response);
        System.out.println(result.toString());
        assertEquals(200, result.getStatusCode().value());
        assertEquals("User deleted successfully", ((ApiResponse<?>) Objects.requireNonNull(result.getBody())).getMessage());
    }

    // Additional tests for validateToken, validateAdmin, etc.
}
