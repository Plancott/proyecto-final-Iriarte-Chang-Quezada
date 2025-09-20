package com.microservices.handler;

import com.microservices.enums.ErrorCode;
import com.microservices.exception.InvalidCredentialsException;
import com.microservices.exception.UserAlreadyExistsException;
import com.microservices.exception.UserNotFoundException;
import com.microservices.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests para GlobalExceptionHandler - Manejo global de excepciones
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle UserNotFoundException correctly")
    void shouldHandleUserNotFoundExceptionCorrectly() {
        // Given
        UserNotFoundException exception = UserNotFoundException.byId(999L);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleUserNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), body.get("error"));
        assertEquals("Usuario no encontrado con ID: 999", body.get("message"));
    }

    @Test
    @DisplayName("Should handle UserAlreadyExistsException by userName correctly")
    void shouldHandleUserAlreadyExistsExceptionByUserNameCorrectly() {
        // Given
        UserAlreadyExistsException exception = UserAlreadyExistsException.byUserName("existinguser");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleUserAlreadyExistsException(exception);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.USER_ALREADY_EXISTS.getCode(), body.get("error"));
        assertEquals("Ya existe un usuario con el nombre de usuario: existinguser", body.get("message"));
    }

    @Test
    @DisplayName("Should handle UserAlreadyExistsException by email correctly")
    void shouldHandleUserAlreadyExistsExceptionByEmailCorrectly() {
        // Given
        UserAlreadyExistsException exception = UserAlreadyExistsException.byEmail("existing@example.com");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleUserAlreadyExistsException(exception);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.USER_ALREADY_EXISTS.getCode(), body.get("error"));
        assertEquals("Ya existe un usuario con el email: existing@example.com", body.get("message"));
    }

    @Test
    @DisplayName("Should handle InvalidCredentialsException correctly")
    void shouldHandleInvalidCredentialsExceptionCorrectly() {
        // Given
        InvalidCredentialsException exception = InvalidCredentialsException.invalidLogin();

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInvalidCredentialsException(exception);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.INVALID_CREDENTIALS.getCode(), body.get("error"));
        assertEquals("Credenciales inválidas. Usuario o contraseña incorrectos.", body.get("message"));
    }

    @Test
    @DisplayName("Should handle ValidationException correctly")
    void shouldHandleValidationExceptionCorrectly() {
        // Given
        ValidationException exception = new ValidationException("Validation failed", ErrorCode.VALIDATION_ERROR);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleValidationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.VALIDATION_ERROR.getCode(), body.get("error"));
        assertEquals("Validation failed", body.get("message"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException correctly")
    void shouldHandleMethodArgumentNotValidExceptionCorrectly() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("userRequestDTO", "userName", "Username is required");
        FieldError fieldError2 = new FieldError("userRequestDTO", "email", "Email format is invalid");
        
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.VALIDATION_ERROR.getCode(), body.get("error"));
        assertEquals("Datos de entrada inválidos", body.get("message"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrors = (Map<String, String>) body.get("fieldErrors");
        assertNotNull(fieldErrors);
        assertEquals("Username is required", fieldErrors.get("userName"));
        assertEquals("Email format is invalid", fieldErrors.get("email"));
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void shouldHandleGenericExceptionCorrectly() {
        // Given
        Exception exception = new RuntimeException("Something went wrong");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.INTERNAL_ERROR.getCode(), body.get("error"));
        assertEquals("Error interno del servidor", body.get("message"));
    }

    @Test
    @DisplayName("Should handle ValidationException without details correctly")
    void shouldHandleValidationExceptionWithoutDetailsCorrectly() {
        // Given
        ValidationException exception = new ValidationException("Simple validation error", ErrorCode.VALIDATION_ERROR);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleValidationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.VALIDATION_ERROR.getCode(), body.get("error"));
        assertEquals("Simple validation error", body.get("message"));
        assertNotNull(body.get("details")); // No details provided
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with no field errors")
    void shouldHandleMethodArgumentNotValidExceptionWithNoFieldErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList());
        
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.VALIDATION_ERROR.getCode(), body.get("error"));
        assertEquals("Datos de entrada inválidos", body.get("message"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrors = (Map<String, String>) body.get("fieldErrors");
        assertNotNull(fieldErrors);
        assertTrue(fieldErrors.isEmpty());
    }

    @Test
    @DisplayName("Should handle null exception message gracefully")
    void shouldHandleNullExceptionMessageGracefully() {
        // Given
        Exception exception = new RuntimeException();

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(ErrorCode.INTERNAL_ERROR.getCode(), body.get("error"));
        assertEquals("Error interno del servidor", body.get("message"));
    }
}
