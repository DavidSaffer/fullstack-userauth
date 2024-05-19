package com.example.UserAuthentication.controller;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.LoginDTO;
import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.dto.SignupDTO;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.expiration.hours}")
    private int jwtExpirationInHours;

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtExpirationInHours * 3600); // Convert hours to seconds
        return cookie;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDTO signupDTO, HttpServletResponse response) {
        logger.info("Signup attempt for username: {}", signupDTO.getUsername());

        String username = signupDTO.getUsername();
        String password = signupDTO.getPassword();
        String email = signupDTO.getEmail();
        String phoneNumber = signupDTO.getPhoneNumber();

        LoginResponse loginResponse = userService.registerNewUser(username, password, email, phoneNumber);
        if (loginResponse.isSuccess()) {
            Cookie cookie = createCookie("jwt", loginResponse.getToken());
            response.addCookie(cookie);
            return ResponseEntity.ok().body(new ApiResponse<>(true, "User registered successfully", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Registration failed", loginResponse.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        logger.info("Login attempt for username: {}", loginDTO.getUsername());
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        LoginResponse loginResponse = userService.login(username, password);
        if (loginResponse.isSuccess()) {
            response.addCookie(createCookie("jwt", loginResponse.getToken()));
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Authentication failed", loginResponse.getMessage()));
    }
}
