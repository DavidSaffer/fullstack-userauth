package com.example.UserAuthentication.service;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.dto.UserDTO;
import com.example.UserAuthentication.enums.UserRoles;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.repository.UserRepository;
import com.example.UserAuthentication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user operations.
 * This includes registration, authentication, user updates, and more, integrating with the UserRepository for data persistence.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String usernameRegex = "^.{4,}$";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user with the provided credentials.
     * Checks for username uniqueness, password strength, and persists the new user to the database.
     *
     * @param username the desired username.
     * @param password the desired password.
     * @param email the user's email.
     * @param phoneNumber the user's phone number.
     * @return LoginResponse indicating the result of the registration attempt.
     */
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

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username the username of the user attempting to log in.
     * @param password the password provided for login.
     * @return LoginResponse indicating the result of the login attempt.
     */
    public LoginResponse login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new LoginResponse(false, null, "User: " + username + " not found");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return new LoginResponse(false, null, "Incorrect password");
        }
        String token = jwtUtil.generateToken(user.get().getUsername(), user.get().getRole().toString());
        return new LoginResponse(true, token, "Login successful");
    }

    /**
     * Edits user details based on provided new information. Ensures that the requesting user has the right to make changes.
     *
     * @param oldUsername the current username of the user to update.
     * @param newUsername the new username, if changed.
     * @param email new email, if changed.
     * @param phoneNumber new phone number, if changed.
     * @param role new role, if changed.
     * @param jwtUsername the username from the JWT token.
     * @param jwtRole the role from the JWT token.
     * @return ApiResponse indicating the success or failure of the update.
     */
    public ApiResponse<?> editUser(String oldUsername, String newUsername, String email, String phoneNumber, String role, String jwtUsername, String jwtRole) {
        //Find the user
        Optional<User> user = userRepository.findByUsername(oldUsername);

        if (user.isEmpty()) {
            return new ApiResponse<String>(false, "User not found", "User not found");
        }
        User foundUser = user.get();
        //Check if this JWT allows them to even update
        // Allowed to update if - it's your user, or if you are an admin
        boolean allowedToUpdate = foundUser.getUsername().equals(jwtUsername) || jwtRole.equals("ADMIN");
        if (!allowedToUpdate) {
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
        String token = jwtUtil.generateToken(user.get().getUsername(), user.get().getRole().toString());
        return new ApiResponse<String>(true, "Success", token);
    }

    /**
     * Validates password strength based on predefined rules.
     *
     * @param password the password to validate.
     * @return true if the password meets the strength requirements, otherwise false.
     */
    private boolean isPasswordStrong(String password) {
        if (password == null) {
            return false;
        }
        // Example requirements: minimum 8 characters, at least one uppercase, one lowercase, and one number
        //String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        String regex = "^.{4,}$";
        return password.matches(regex);
    }

    /**
     * Retrieves all users if the requester has administrative privileges.
     *
     * @param role the role of the requester.
     * @return ApiResponse containing a list of all users or an error message.
     */
    public ApiResponse<?> getAllUsers(String role) {
        if (!role.equals("ADMIN")) {
            return new ApiResponse<>(false, "Invalid role: "+role, role);
        }
        List<UserDTO> users = userRepository.findAllUsersWithTruncatedPasswords();
        return new ApiResponse<>(true, "Success", users);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to find.
     * @return User object if found, null otherwise.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Deletes a user from the system based on username, verifying permissions from JWT claims.
     *
     * @param username the username of the user to delete.
     * @param jwtUsername the username from the JWT token.
     * @param jwtRole the role from the JWT token.
     * @return ApiResponse indicating the result of the deletion attempt.
     */
    public ApiResponse<?> deleteUser(String username, String jwtUsername, String jwtRole) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new ApiResponse<>(false, "User "+ username +" not found", "User "+ username +" not found");
        }
        boolean allowedToUpdate = user.get().getUsername().equals(jwtUsername) || jwtRole.equals("ADMIN");
        if (!allowedToUpdate){
            return new ApiResponse<>(false, "Invalid Permissions in JWT token", "Invalid Permissions in JWT token");
        }
        boolean isDeletingSelf = user.get().getUsername().equals(jwtUsername);
        try {
            userRepository.delete(user.get());
            if (isDeletingSelf) {
                return new ApiResponse<>(true, "Success", true);
            }
            return new ApiResponse<>(true, "Success", false);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to delete user", "Failed to delete user");
        }
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
}
