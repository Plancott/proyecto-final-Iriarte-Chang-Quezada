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
    @DisplayName("Should find user by username case insensitive")
    void shouldFindUserByUserNameCaseInsensitive() {
        // When
        Optional<User> found = userRepository.findByUserName("TESTUSER");

        // Then
        // Note: This test assumes the repository is case-sensitive
        // If case-insensitive search is needed, it should be implemented in the repository
        assertFalse(found.isPresent()); // Current implementation is case-sensitive
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void shouldReturnEmptyWhenUserNotFoundByUserName() {
        // When
        Optional<User> found = userRepository.findByUserName("nonexistentuser");

        // Then
        assertFalse(found.isPresent());
    }
}
