package com.microservices.dto;

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
 * Test class for LoginDTO
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginDTO Tests")
class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create valid LoginDTO")
    void shouldCreateValidLoginDTO() {
        // Given & When
        LoginDTO loginDTO = LoginDTO.builder()
                .userName("testuser")
                .password("password123")
                .build();

        // Then
        assertNotNull(loginDTO);
        assertEquals("testuser", loginDTO.getUserName());
        assertEquals("password123", loginDTO.getPassword());
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        // Given
        LoginDTO validLogin = LoginDTO.builder()
                .userName("testuser")
                .password("password123")
                .build();

        // When
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(validLogin);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation with blank userName")
    void shouldFailValidationWithBlankUserName() {
        // Given
        LoginDTO invalidLogin = LoginDTO.builder()
                .userName("")
                .password("password123")
                .build();

        // When
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(invalidLogin);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
    }

    @Test
    @DisplayName("Should fail validation with blank password")
    void shouldFailValidationWithBlankPassword() {
        // Given
        LoginDTO invalidLogin = LoginDTO.builder()
                .userName("testuser")
                .password("")
                .build();

        // When
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(invalidLogin);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Should fail validation with short password")
    void shouldFailValidationWithShortPassword() {
        // Given
        LoginDTO invalidLogin = LoginDTO.builder()
                .userName("testuser")
                .password("123")
                .build();

        // When
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(invalidLogin);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Should create LoginDTO with constructor")
    void shouldCreateLoginDTOWithConstructor() {
        // Given & When
        LoginDTO loginDTO = new LoginDTO("testuser", "password123");

        // Then
        assertNotNull(loginDTO);
        assertEquals("testuser", loginDTO.getUserName());
        assertEquals("password123", loginDTO.getPassword());
    }

    @Test
    @DisplayName("Should create empty LoginDTO with no-args constructor")
    void shouldCreateEmptyLoginDTOWithNoArgsConstructor() {
        // When
        LoginDTO emptyLogin = new LoginDTO();

        // Then
        assertNotNull(emptyLogin);
        assertNull(emptyLogin.getUserName());
        assertNull(emptyLogin.getPassword());
    }
}
