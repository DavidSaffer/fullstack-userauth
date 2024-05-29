package com.example.UserAuthentication.repository;

import com.example.UserAuthentication.dto.UserDTO;
import com.example.UserAuthentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 * Provides the mechanism for storage, retrieval, update, and delete operations on User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find.
     * @return an {@link Optional} of {@link User}, which will be empty if no user is found.
     */
    Optional<User> findByUsername(@NonNull String username);

    /**
     * Retrieves all users from the database with their passwords truncated for security reasons.
     * The password is truncated to the first 10 characters.
     *
     * @return a list of {@link UserDTO} with truncated passwords.
     */
    @Query("SELECT new com.example.UserAuthentication.dto.UserDTO(u.userId, u.username, SUBSTRING(u.password, 1, 10), u.role, u.email, u.phoneNumber, u.dateCreated, u.dateUpdated) FROM User u")
    List<UserDTO> findAllUsersWithTruncatedPasswords();
}

