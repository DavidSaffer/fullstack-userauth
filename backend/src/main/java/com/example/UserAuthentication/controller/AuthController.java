package com.example.UserAuthentication.controller;

import com.example.UserAuthentication.dto.*;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.service.UserService;
import com.example.UserAuthentication.utility.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller that handles authentication processes such as signup, login, logout, token validation, and user deletion.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.expiration.hours}")
    private int jwtExpirationInHours;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates a secure, HTTP-only cookie containing a JWT.
     *
     * @param name  the name of the cookie.
     * @param value the JWT value (token) to store in the cookie.
     * @return a configured Cookie object.
     */
    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtExpirationInHours * 3600); // Convert hours to seconds
        return cookie;
    }

    /**
     * Creates an empty, expiring cookie to facilitate logout.
     *
     * @return an expired Cookie object.
     */
    private Cookie createEmptyCookie() {
        Cookie cookie = new Cookie("jwt", null); // null value for the cookie
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // should be true in production
        cookie.setPath("/");
        cookie.setMaxAge(0); // expires immediately
        return cookie;
    }

    /**
     * Registers a new user with provided credentials and returns a JWT token in a cookie.
     *
     * @param signupDTO the data transfer object containing signup details.
     * @param response  the HttpServletResponse to add the cookie to.
     * @return ResponseEntity indicating the outcome of the registration attempt.
     */
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
            return ResponseEntity.ok().body(new ApiResponse<>(true, "User registered successfully", loginResponse.getToken()));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Registration failed", loginResponse.getMessage()));
    }

    /**
     * Authenticates a user and returns a JWT token in a cookie if successful.
     *
     * @param loginDTO the data transfer object containing login credentials.
     * @param response the HttpServletResponse to add the cookie to.
     * @return ResponseEntity indicating the outcome of the login attempt.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        logger.info("Login attempt for username: {}", loginDTO.getUsername());
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        LoginResponse loginResponse = userService.login(username, password);
        if (loginResponse.isSuccess()) {
            response.addCookie(createCookie("jwt", loginResponse.getToken()));
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", loginResponse.getToken()));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Authentication failed", loginResponse.getMessage()));
    }

    /**
     * Logs out a user by invalidating the JWT token cookie.
     *
     * @param response the HttpServletResponse to add the expiring cookie to.
     * @return ResponseEntity indicating successful logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create a cookie that overwrites the existing JWT cookie with an expired one
        Cookie cookie = new Cookie("jwt", null); // null value for the cookie
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // should be true in production
        cookie.setPath("/");
        cookie.setMaxAge(0); // expires immediately
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Validates a JWT token for authenticity and expiration.
     *
     * @param token the JWT token to validate.
     * @return ResponseEntity indicating if the token is valid or not.
     */
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        try {
            boolean isValid = jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token);
            if (isValid) {
                return ResponseEntity.ok().body(new ApiResponse<>(true, "Token is valid", null));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Token is invalid", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error validating token", null));
        }
    }

    /**
     * Checks if the bearer of the provided JWT token is an administrator.
     *
     * @param token the JWT token to check.
     * @return ResponseEntity indicating if the user is an admin or not.
     */
    @PostMapping("/is-admin")
    public ResponseEntity<?> validateAdmin(@RequestBody String token) {
        try {
            String role = jwtUtil.getRoleFromToken(token);
            boolean isAdmin = "ADMIN".equals(role);
            if (isAdmin) {
                return ResponseEntity.ok(new ApiResponse<>(true, "User is Admin", null));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Access denied. User is not an admin.", role));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error validating user role: " + e.getMessage(), token));
        }
    }

    /**
     * Deletes a user identified by username, provided the requester has appropriate authorization.
     * User making this request must either | be an admin or be the user that is being deleted
     *
     * @param username the username of the user to delete.
     * @param request  the HttpServletRequest containing the JWT cookie.
     * @param response the HttpServletResponse to add an expiring cookie to if the user is deleting themselves.
     * @return ResponseEntity indicating the outcome of the deletion attempt.
     */
    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        // 1 - Get the jwt Cookie
        try {
            Cookie[] cookies = request.getCookies();
            String jwtToken = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
            if (jwtToken == null) {
                return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied: No JWT token found.", null));
            }
            // 2 - Extract the username and role from the cookie
            // (this shows us who is making this request)
            // (used to verify if the person is allowed to delete or not)
            String jwtUsername = jwtUtil.getUsernameFromToken(jwtToken);
            String jwtRole = jwtUtil.getRoleFromToken(jwtToken);
            // 3 - Pass this information to user service, and handle the result
            ApiResponse<?> result = userService.deleteUser(username, jwtUsername, jwtRole);
            // 4 - If deleting self, remove the cookie (override it)
            if (result.isSuccess()) {
                if (result.getData().equals(true)){
                    Cookie emptyCookie = createEmptyCookie();
                    response.addCookie(emptyCookie);
                    return ResponseEntity.ok().body(new ApiResponse<>(true, "User deleted successfully", true)); // True for we deleted ourselves
                }
                return ResponseEntity.ok().body(new ApiResponse<>(true, "User deleted successfully", false)); // False for we deleted someone else
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, result.getMessage(), result.getData()));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error deleting user", e.getMessage()));
        }
    }

}
