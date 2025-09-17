package com.microservices.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User entity validation using Jakarta Validation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Entity Validation Tests")
class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation with valid user data")
    void shouldPassValidationWithValidUserData() {
        // Given
        User validUser = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation with blank userName")
    void shouldFailValidationWithBlankUserName() {
        // Given
        User userWithBlankUsername = User.builder()
                .userName("")
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithBlankUsername);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
    }

    @Test
    @DisplayName("Should fail validation with null userName")
    void shouldFailValidationWithNullUserName() {
        // Given
        User userWithNullUsername = User.builder()
                .userName(null)
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithNullUsername);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
    }

    @Test
    @DisplayName("Should fail validation with invalid email format")
    void shouldFailValidationWithInvalidEmailFormat() {
        // Given
        User userWithInvalidEmail = User.builder()
                .userName("validuser")
                .email("invalid-email")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithInvalidEmail);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Should fail validation with short password")
    void shouldFailValidationWithShortPassword() {
        // Given
        User userWithShortPassword = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("123") // Too short
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithShortPassword);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Should fail validation with blank password")
    void shouldFailValidationWithBlankPassword() {
        // Given
        User userWithBlankPassword = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithBlankPassword);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Should fail validation with invalid name pattern")
    void shouldFailValidationWithInvalidNamePattern() {
        // Given
        User userWithInvalidName = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("validpassword123")
                .name("John123") // Invalid pattern - contains numbers
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithInvalidName);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("Should fail validation with invalid lastName pattern")
    void shouldFailValidationWithInvalidLastNamePattern() {
        // Given
        User userWithInvalidLastName = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User@") // Invalid pattern - contains special characters
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithInvalidLastName);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    @DisplayName("Should fail validation with null role")
    void shouldFailValidationWithNullRole() {
        // Given
        User userWithNullRole = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(null)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithNullRole);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("role")));
    }

    @Test
    @DisplayName("Should pass validation with valid admin role")
    void shouldPassValidationWithValidAdminRole() {
        // Given
        User adminUser = User.builder()
                .userName("adminuser")
                .email("admin@example.com")
                .password("adminpassword123")
                .name("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(adminUser);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should pass validation with special characters in name")
    void shouldPassValidationWithSpecialCharactersInName() {
        // Given
        User userWithSpecialChars = User.builder()
                .userName("validuser")
                .email("valid@example.com")
                .password("validpassword123")
                .name("José María") // Valid - contains Spanish characters
                .lastName("González")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithSpecialChars);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation with userName too long")
    void shouldFailValidationWithUserNameTooLong() {
        // Given
        String longUserName = "a".repeat(51); // 51 characters - exceeds limit
        User userWithLongUsername = User.builder()
                .userName(longUserName)
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithLongUsername);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
    }

    @Test
    @DisplayName("Should fail validation with userName too short")
    void shouldFailValidationWithUserNameTooShort() {
        // Given
        User userWithShortUsername = User.builder()
                .userName("ab") // 2 characters - below minimum
                .email("valid@example.com")
                .password("validpassword123")
                .name("Valid")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(userWithShortUsername);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
    }
}
