package com.example.UserAuthentication.repository;

import com.example.UserAuthentication.dto.UserDTO;
import com.example.UserAuthentication.enums.UserRoles;
import com.example.UserAuthentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setup() {
        // Setup data for each test
        User user = new User();
        user.setUsername("johnDoe");
        user.setPassword("password123");
        user.setRole(UserRoles.ADMIN);
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("123-456-7890");
        user.setDateCreated(LocalDateTime.now());
        user.setDateUpdated(LocalDateTime.now());
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindByUsername_thenUserShouldBeFound() {
        Optional<User> found = userRepository.findByUsername("johnDoe");
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUsername()).isEqualTo("johnDoe");
    }

    @Test
    public void whenFindAllUsersWithTruncatedPasswords_thenUsersShouldBeReturnedWithTruncatedPasswords() {
        List<UserDTO> users = userRepository.findAllUsersWithTruncatedPasswords();
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getPassword()).hasSize(10);
    }
}
