package com.example.UserAuthentication.service;

import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.repository.UserRepository;
import com.example.UserAuthentication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse registerNewUser(String username, String password, String email, String phoneNumber) {
        // Check if the username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return new LoginResponse(false, null, "Username: " + username + " is already taken");
        }
        //String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        String regex = "^.{4,}$";
        if (!username.matches(regex)) {
            return new LoginResponse(false, null, "Username too short. Must be at least 4 characters");
        }

        // Check if the password meets the strength requirements
        if (!isPasswordStrong(password)) {
            return new LoginResponse(false, null, "Password does not meet the security requirements. Must be at least 4 characters");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setDateCreated(LocalDateTime.now());
        user.setDateUpdated(LocalDateTime.now());
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }
        userRepository.save(user);

        // Generate a token for the new user
        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(true, token, "Registration successful");
    }

    public LoginResponse login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            return new LoginResponse(false, null, "User: " + username + " not found");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return new LoginResponse(false, null, "Incorrect password");
        }
        String token = jwtUtil.generateToken(user.get().getUsername());
        return new LoginResponse(true, token, "Login successful");
    }

    private boolean isPasswordStrong(String password) {
        if (password == null) {
            return false;
        }
        // Example requirements: minimum 8 characters, at least one uppercase, one lowercase, and one number
        //String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        String regex = "^.{4,}$";
        return password.matches(regex);
    }

    private boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        }
        String regex = "^.{4,}$";
        return username.matches(regex);
    }

}
