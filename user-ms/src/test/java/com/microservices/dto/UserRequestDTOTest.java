package com.microservices.dto;

import com.microservices.entity.UserRole;
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
 * Test class for UserRequestDTO
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserRequestDTO Tests")
class UserRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create valid UserRequestDTO")
    void shouldCreateValidUserRequestDTO() {
        // Given & When
        UserRequestDTO userRequest = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // Then
        assertNotNull(userRequest);
        assertEquals("testuser", userRequest.getUserName());
        assertEquals("test@example.com", userRequest.getEmail());
        assertEquals("password123", userRequest.getPassword());
        assertEquals("Test", userRequest.getName());
        assertEquals("User", userRequest.getLastName());
        assertEquals(UserRole.USER, userRequest.getRole());
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        // Given
        UserRequestDTO validRequest = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(validRequest);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation with blank userName")
    void shouldFailValidationWithBlankUserName() {
        // Given
        UserRequestDTO invalidRequest = UserRequestDTO.builder()
                .userName("")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidRequest);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
    }

    @Test
    @DisplayName("Should fail validation with invalid email")
    void shouldFailValidationWithInvalidEmail() {
        // Given
        UserRequestDTO invalidRequest = UserRequestDTO.builder()
                .userName("testuser")
                .email("invalid-email")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidRequest);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Should fail validation with short password")
    void shouldFailValidationWithShortPassword() {
        // Given
        UserRequestDTO invalidRequest = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidRequest);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Should fail validation with null role")
    void shouldFailValidationWithNullRole() {
        // Given
        UserRequestDTO invalidRequest = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(null)
                .build();

        // When
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidRequest);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("role")));
    }
}
