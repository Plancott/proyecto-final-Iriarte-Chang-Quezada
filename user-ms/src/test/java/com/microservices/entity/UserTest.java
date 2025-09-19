package com.microservices.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User entity using JUnit 5 and Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Entity Tests")
class UserTest {

    private User user;
    private static final String USERNAME = "testuser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final UserRole ROLE = UserRole.USER;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userName(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .lastName(LAST_NAME)
                .role(ROLE)
                .build();
    }

    @Test
    @DisplayName("Should create user with all parameters using builder pattern")
    void shouldCreateUserWithBuilderPattern() {
        // Given & When
        User newUser = User.builder()
                .userName("johndoe")
                .email("john@email.com")
                .password("secret123")
                .name("John")
                .lastName("Doe")
                .role(UserRole.ADMIN)
                .build();

        // Then
        assertNotNull(newUser);
        assertEquals("johndoe", newUser.getUserName());
        assertEquals("john@email.com", newUser.getEmail());
        assertEquals("secret123", newUser.getPassword());
        assertEquals("John", newUser.getName());
        assertEquals("Doe", newUser.getLastName());
        assertEquals(UserRole.ADMIN, newUser.getRole());
    }

    @Test
    @DisplayName("Should create user with constructor")
    void shouldCreateUserWithConstructor() {
        // Given & When
        User newUser = new User("jane", "jane@email.com", "pass123", "Jane", "Smith", UserRole.USER);

        // Then
        assertNotNull(newUser);
        assertEquals("jane", newUser.getUserName());
        assertEquals("jane@email.com", newUser.getEmail());
        assertEquals("pass123", newUser.getPassword());
        assertEquals("Jane", newUser.getName());
        assertEquals("Smith", newUser.getLastName());
        assertEquals(UserRole.USER, newUser.getRole());
    }

    @Test
    @DisplayName("Should create empty user with no-args constructor")
    void shouldCreateEmptyUserWithNoArgsConstructor() {
        // Given & When
        User emptyUser = new User();

        // Then
        assertNotNull(emptyUser);
        assertNull(emptyUser.getUserName());
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getPassword());
        assertNull(emptyUser.getName());
        assertNull(emptyUser.getLastName());
        assertNull(emptyUser.getRole());
    }

    @Test
    @DisplayName("Should set and get userId")
    void shouldSetAndGetUserId() {
        // Given
        Long userId = 1L;

        // When
        user.setUserId(userId);

        // Then
        assertEquals(userId, user.getUserId());
    }

    @Test
    @DisplayName("Should set and get userName")
    void shouldSetAndGetUserName() {
        // Given
        String newUsername = "newuser";

        // When
        user.setUserName(newUsername);

        // Then
        assertEquals(newUsername, user.getUserName());
    }

    @Test
    @DisplayName("Should set and get email")
    void shouldSetAndGetEmail() {
        // Given
        String newEmail = "newemail@example.com";

        // When
        user.setEmail(newEmail);

        // Then
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void shouldSetAndGetPassword() {
        // Given
        String newPassword = "newpassword123";

        // When
        user.setPassword(newPassword);

        // Then
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        // Given
        String newName = "Jane";

        // When
        user.setName(newName);

        // Then
        assertEquals(newName, user.getName());
    }

    @Test
    @DisplayName("Should set and get lastName")
    void shouldSetAndGetLastName() {
        // Given
        String newLastName = "Smith";

        // When
        user.setLastName(newLastName);

        // Then
        assertEquals(newLastName, user.getLastName());
    }

    @Test
    @DisplayName("Should set and get role")
    void shouldSetAndGetRole() {
        // Given
        UserRole newRole = UserRole.ADMIN;

        // When
        user.setRole(newRole);

        // Then
        assertEquals(newRole, user.getRole());
    }

    @Test
    @DisplayName("Should set and get registerDate")
    void shouldSetAndGetRegisterDate() {
        // Given
        LocalDateTime registerDate = LocalDateTime.now();

        // When
        user.setRegisterDate(registerDate);

        // Then
        assertEquals(registerDate, user.getRegisterDate());
    }

    @Test
    @DisplayName("Should return correct equals for same userId and email")
    void shouldReturnCorrectEqualsForSameUserIdAndEmail() {
        // Given
        User user1 = User.builder()
                .userId(1L)
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .userId(1L)
                .email("test@example.com")
                .userName("different")
                .build();

        User user3 = User.builder()
                .userId(2L)
                .email("test@example.com")
                .build();

        // When & Then
        assertEquals(user1, user2); // Same userId and email
        assertNotEquals(user1, user3); // Different userId
    }

    @Test
    @DisplayName("Should return correct hashCode for same userId and email")
    void shouldReturnCorrectHashCodeForSameUserIdAndEmail() {
        // Given
        User user1 = User.builder()
                .userId(1L)
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .userId(1L)
                .email("test@example.com")
                .userName("different")
                .build();

        // When & Then
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Should exclude password from toString")
    void shouldExcludePasswordFromToString() {
        // Given
        user.setPassword("secretpassword");

        // When
        String userString = user.toString();

        // Then
        assertNotNull(userString);
        assertFalse(userString.contains("secretpassword"));
        assertTrue(userString.contains("testuser"));
        assertTrue(userString.contains("test@example.com"));
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // Given
        User nullUser = new User();

        // When & Then
        assertNull(nullUser.getUserId());
        assertNull(nullUser.getUserName());
        assertNull(nullUser.getEmail());
        assertNull(nullUser.getPassword());
        assertNull(nullUser.getName());
        assertNull(nullUser.getLastName());
        assertNull(nullUser.getRole());
        assertNull(nullUser.getRegisterDate());
    }

    @Test
    @DisplayName("Should create user with all fields populated")
    void shouldCreateUserWithAllFieldsPopulated() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Long userId = 100L;

        // When
        User fullUser = User.builder()
                .userId(userId)
                .userName("fulluser")
                .email("full@example.com")
                .password("fullpassword")
                .name("Full")
                .lastName("User")
                .role(UserRole.ADMIN)
                .registerDate(now)
                .build();

        // Then
        assertEquals(userId, fullUser.getUserId());
        assertEquals("fulluser", fullUser.getUserName());
        assertEquals("full@example.com", fullUser.getEmail());
        assertEquals("fullpassword", fullUser.getPassword());
        assertEquals("Full", fullUser.getName());
        assertEquals("User", fullUser.getLastName());
        assertEquals(UserRole.ADMIN, fullUser.getRole());
        assertEquals(now, fullUser.getRegisterDate());
    }
}

