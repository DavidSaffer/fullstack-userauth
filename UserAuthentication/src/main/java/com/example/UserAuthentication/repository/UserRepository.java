package com.example.UserAuthentication.repository;

import com.example.UserAuthentication.dto.UserDTO;
import com.example.UserAuthentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NonNull String username);

    @Query("SELECT new com.example.UserAuthentication.dto.UserDTO(u.userId, u.username, SUBSTRING(u.password, 1, 10), u.role, u.email, u.phoneNumber, u.dateCreated, u.dateUpdated) FROM User u")
    List<UserDTO> findAllUsersWithTruncatedPasswords();
}

