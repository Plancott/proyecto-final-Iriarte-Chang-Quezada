package com.microservices.repository;

import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserRepository
 */
@DataJpaTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = User.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        adminUser = User.builder()
                .userName("adminuser")
                .email("admin@example.com")
                .password("adminpass123")
                .name("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .registerDate(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(adminUser);
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUserName() {
        // When
        Optional<User> found = userRepository.findByUserName("testuser");

        // Then
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUserName());
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Should check if user exists by username")
    void shouldCheckIfUserExistsByUserName() {
        // When & Then
        assertTrue(userRepository.existsByUserName("testuser"));
        assertFalse(userRepository.existsByUserName("nonexistent"));
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckIfUserExistsByEmail() {
        // When & Then
        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Should find users by role")
    void shouldFindUsersByRole() {
        // When
        List<User> userRoleUsers = userRepository.findByRole(UserRole.USER);
        List<User> adminRoleUsers = userRepository.findByRole(UserRole.ADMIN);

        // Then
        assertEquals(1, userRoleUsers.size());
        assertEquals(1, adminRoleUsers.size());
    }

    @Test
    @DisplayName("Should save and retrieve user")
    void shouldSaveAndRetrieveUser() {
        // Given
        User newUser = User.builder()
                .userName("newuser")
                .email("new@example.com")
                .password("newpass123")
                .name("New")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        // When
        User saved = userRepository.save(newUser);
        Optional<User> retrieved = userRepository.findById(saved.getUserId());

        // Then
        assertTrue(retrieved.isPresent());
        assertEquals("newuser", retrieved.get().getUserName());
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        // Given
        Long userId = testUser.getUserId();

        // When
        userRepository.delete(testUser);

        // Then
        Optional<User> deleted = userRepository.findById(userId);
        assertFalse(deleted.isPresent());
    }

    @Test
    @DisplayName("Should find user by username and password")
    void shouldFindUserByUserNameAndPassword() {
        // When
        Optional<User> found = userRepository.findByUserNameAndPassword("testuser", "password123");

        // Then
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUserName());
        assertEquals("password123", found.get().getPassword());
    }

    @Test
    @DisplayName("Should return empty when username and password don't match")
    void shouldReturnEmptyWhenUserNameAndPasswordDontMatch() {
        // When
        Optional<User> found = userRepository.findByUserNameAndPassword("testuser", "wrongpassword");

        // Then
        assertFalse(found.isPresent());
    }
}
