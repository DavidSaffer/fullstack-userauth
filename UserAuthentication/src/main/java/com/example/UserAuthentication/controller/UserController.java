package com.example.UserAuthentication.controller;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.EditUserDTO;
import com.example.UserAuthentication.dto.UserInfoDTO;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.service.UserService;
import com.example.UserAuthentication.utility.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration.hours}")
    private int jwtExpirationInHours;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtExpirationInHours * 3600); // Convert hours to seconds
        return cookie;
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(@CookieValue("jwt") String token) {
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            if (username != null && jwtUtil.validateToken(token)) {
                User user = userService.findByUsername(username);
                if (user != null) {
                    UserInfoDTO userInfo = new UserInfoDTO(
                            user.getUsername(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getRole().toString()
                    );
                    return ResponseEntity.ok().body(new ApiResponse<>(true, "User information retrieved successfully", userInfo));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid token", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error retrieving user information", null));
        }
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody EditUserDTO editUserDTO,HttpServletRequest request, HttpServletResponse response) {
        try {
            String jwt = extractJwtFromCookie(request);
            if (jwt == null || !jwtUtil.validateToken(jwt)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid or missing JWT token", null));
            }

            String jwtUsername = jwtUtil.getUsernameFromToken(jwt);
            String jwtRole = jwtUtil.getRoleFromToken(jwt);
            // 2 Cases - User updating their own - or admin updating another
            boolean needsCookie = jwtUsername.equals(editUserDTO.getOldUsername());
            ApiResponse<?> result = userService.editUser(editUserDTO.getOldUsername(), editUserDTO.getNewUsername(), editUserDTO.getEmail(), editUserDTO.getPhoneNumber(), editUserDTO.getRole(), jwtUsername, jwtRole);
            if (result.isSuccess()) {
                String token = (String) result.getData();
                if (needsCookie) {
                    response.addCookie(createCookie("jwt", token));
                }
                return ResponseEntity.ok().body(new ApiResponse<>(true, "User updated successfully", null));
            }
            else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, result.getMessage(), result.getData()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error updating user", "Error updating user"));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(HttpServletRequest request) {
        try {
            String jwt = extractJwtFromCookie(request);
            if (jwt == null || !jwtUtil.validateToken(jwt)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid or missing JWT token", null));
            }
            String jwtRole = jwtUtil.getRoleFromToken(jwt);
            if (!"ADMIN".equals(jwtRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Access denied", null));
            }
            ApiResponse<?> result = userService.getAllUsers(jwtRole);
            if (result.isSuccess()) {
                return ResponseEntity.ok().body(new ApiResponse<>(true, "Success", result.getData()));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, "Access denied", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error fetching users", null));
        }
    }


}
