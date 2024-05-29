package com.example.UserAuthentication.model;

import com.example.UserAuthentication.enums.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserDefaultValues() {
        assertNull(user.getUserId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertEquals(UserRoles.USER, user.getRole());
        assertNull(user.getEmail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getDateCreated());
        assertNull(user.getDateUpdated());
    }

    @Test
    void testSetUsername() {
        user.setUsername(" John Doe ");
        assertEquals("John Doe", user.getUsername());
    }

    @Test
    void testSetUsernameNull() {
        user.setUsername(null);
        assertNull(user.getUsername());
    }

    @Test
    void testSetRoleWithEnum() {
        user.setRole(UserRoles.ADMIN);
        assertEquals(UserRoles.ADMIN, user.getRole());
    }

    @Test
    void testSetRoleWithStringValid() {
        user.setRole("admin");
        assertEquals(UserRoles.ADMIN, user.getRole());
    }

    @Test
    void testSetRoleWithStringInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setRole("invalid_role");
        });
        assertTrue(exception.getMessage().contains("Invalid role: 'invalid_role'"));
    }

    @Test
    void testPreUpdate() {
        // Truncate the LocalDateTime to milliseconds to match the precision of Timestamp conversion
        LocalDateTime beforeUpdate = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        user.onUpdate(); // Assume this method sets dateUpdated to now with Timestamp precision

        LocalDateTime afterUpdate = user.getDateUpdated();

        System.out.println("After Update: " + afterUpdate);
        System.out.println("Before Update: " + beforeUpdate);

        // Assert that the afterUpdate is not before beforeUpdate
        assertFalse(afterUpdate.isBefore(beforeUpdate));
    }

//    @Test
//    void testNotBlankConstraints() {
//        User newUser = new User();
//        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
//            newUser.setUsername("");  // Assuming a validation framework is catching this
//            newUser.setPassword("");  // Assuming a validation framework is catching this
//        });
//    }
}

