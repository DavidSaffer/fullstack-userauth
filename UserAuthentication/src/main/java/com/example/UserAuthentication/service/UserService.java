package com.example.UserAuthentication.service;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.enums.UserRoles;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.repository.UserRepository;
import com.example.UserAuthentication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String usernameRegex = "^.{4,}$";

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
        if (!username.matches(usernameRegex)) {
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
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new LoginResponse(false, null, "Something went wrong");
        }

        // Generate a token for the new user
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
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
        String token = jwtUtil.generateToken(user.get().getUsername(), user.get().getRole().toString());
        return new LoginResponse(true, token, "Login successful");
    }

    public ApiResponse<?> editUser(String oldUsername, String newUsername, String email, String phoneNumber, String role, String jwtUsername, String jwtRole) {
        //Find the user
        Optional<User> user = userRepository.findByUsername(oldUsername);

        if (!user.isPresent()) {
            return new ApiResponse<String>(false, "User not found", "User not found");
        }
        User foundUser = user.get();
        //Check if this JWT allows them to even update
        if (!(foundUser.getUsername().equals(jwtUsername) || jwtRole == "ADMIN")) {
            return new ApiResponse<String>(false, "Invalid Permissions", "JWT claims dont match requirements");
        }

        //Update Username
        if(!oldUsername.equals(newUsername)) {
            //Check if username is available
            Optional<User> takenUsername = userRepository.findByUsername(newUsername);
            if(takenUsername.isPresent()) {
                return new ApiResponse<>(false, "Username already taken", "Username already taken");
            }
            //Check if it meets requirements
            if (!newUsername.matches(usernameRegex)) {
                return new ApiResponse<String>(false, "Username too short. Must be at least 4 characters", newUsername);
            }
            user.get().setUsername(newUsername);
        }
        // Update email
        if (email != null && isEmailValid(email)) {
            user.get().setEmail(email);
        }
        // Update phone number
        if (phoneNumber != null && isValidPhoneNumber(phoneNumber)) {
            user.get().setPhoneNumber(phoneNumber);
        }
        // Update role
        if (role != null){
            try {
                user.get().setRole(role);
            } catch (IllegalArgumentException e) {
                return new ApiResponse<String>(false, "Invalid role", "Invalid role");
            }
        }
        try {
            userRepository.save(user.get());
        } catch (Exception e) {
            return new ApiResponse<String>(false, "Failed to save user", "Failed to save user");
        }
        return new ApiResponse<User>(true, "Success", user.get());
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


    // TODO: validate email
    private boolean isEmailValid(String email) {
        return true;
    }

    // TODO: validate phone number
    private boolean isValidPhoneNumber(String phoneNumber) {
        return true;
    }

    private Pair<Boolean, String> isValidUsername(String username) {
        if (username == null) {
            return Pair.of(false, "Username cannot be null.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return Pair.of(false, "Username already in use");
        }
        if (!username.matches(usernameRegex)) {
            return Pair.of(false, "Username too short");
        }
        return Pair.of(true, "Success");
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
