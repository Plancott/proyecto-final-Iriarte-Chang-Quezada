package com.microservices.dto;

import com.microservices.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserResponseDTO
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserResponseDTO Tests")
class UserResponseDTOTest {

    @Test
    @DisplayName("Should create UserResponseDTO with builder pattern")
    void shouldCreateUserResponseDTOWithBuilderPattern() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Long userId = 1L;

        // When
        UserResponseDTO userResponse = UserResponseDTO.builder()
                .userId(userId)
                .userName("testuser")
                .email("test@example.com")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(now)
                .build();

        // Then
        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals("testuser", userResponse.getUserName());
        assertEquals("test@example.com", userResponse.getEmail());
        assertEquals("Test", userResponse.getName());
        assertEquals("User", userResponse.getLastName());
        assertEquals(UserRole.USER, userResponse.getRole());
        assertEquals(now, userResponse.getRegisterDate());
    }

    @Test
    @DisplayName("Should create UserResponseDTO with constructor")
    void shouldCreateUserResponseDTOWithConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Long userId = 1L;

        // When
        UserResponseDTO userResponse = new UserResponseDTO(
                userId,
                "testuser",
                "test@example.com",
                "Test",
                "User",
                UserRole.USER,
                now
        );

        // Then
        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals("testuser", userResponse.getUserName());
        assertEquals("test@example.com", userResponse.getEmail());
        assertEquals("Test", userResponse.getName());
        assertEquals("User", userResponse.getLastName());
        assertEquals(UserRole.USER, userResponse.getRole());
        assertEquals(now, userResponse.getRegisterDate());
    }

    @Test
    @DisplayName("Should create empty UserResponseDTO with no-args constructor")
    void shouldCreateEmptyUserResponseDTOWithNoArgsConstructor() {
        // When
        UserResponseDTO emptyResponse = new UserResponseDTO();

        // Then
        assertNotNull(emptyResponse);
        assertNull(emptyResponse.getUserId());
        assertNull(emptyResponse.getUserName());
        assertNull(emptyResponse.getEmail());
        assertNull(emptyResponse.getName());
        assertNull(emptyResponse.getLastName());
        assertNull(emptyResponse.getRole());
        assertNull(emptyResponse.getRegisterDate());
    }

    @Test
    @DisplayName("Should handle admin role correctly")
    void shouldHandleAdminRoleCorrectly() {
        // Given & When
        UserResponseDTO adminResponse = UserResponseDTO.builder()
                .userId(1L)
                .userName("admin")
                .email("admin@example.com")
                .name("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .registerDate(LocalDateTime.now())
                .build();

        // Then
        assertEquals(UserRole.ADMIN, adminResponse.getRole());
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // When
        UserResponseDTO response = UserResponseDTO.builder()
                .userId(null)
                .userName(null)
                .email(null)
                .name(null)
                .lastName(null)
                .role(null)
                .registerDate(null)
                .build();

        // Then
        assertNull(response.getUserId());
        assertNull(response.getUserName());
        assertNull(response.getEmail());
        assertNull(response.getName());
        assertNull(response.getLastName());
        assertNull(response.getRole());
        assertNull(response.getRegisterDate());
    }
}
