package com.microservices.mapper;

import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;
import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserMapper using MapStruct
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    @DisplayName("Should convert UserRequestDTO to User entity")
    void shouldConvertUserRequestDTOToUserEntity() {
        // Given
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When
        User user = userMapper.toEntity(userRequestDTO);

        // Then
        assertNotNull(user);
        assertNull(user.getUserId()); // Should be ignored
        assertEquals("testuser", user.getUserName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Test", user.getName());
        assertEquals("User", user.getLastName());
        assertEquals(UserRole.USER, user.getRole());
        assertNull(user.getRegisterDate()); // Should be ignored
    }

    @Test
    @DisplayName("Should convert User entity to UserResponseDTO")
    void shouldConvertUserEntityToUserResponseDTO() {
        // Given
        User user = User.builder()
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        // When
        UserResponseDTO userResponseDTO = userMapper.toResponseDTO(user);

        // Then
        assertNotNull(userResponseDTO);
        assertEquals(1L, userResponseDTO.getUserId());
        assertEquals("testuser", userResponseDTO.getUserName());
        assertEquals("test@example.com", userResponseDTO.getEmail());
        // Password is not included in UserResponseDTO for security reasons
        assertEquals("Test", userResponseDTO.getName());
        assertEquals("User", userResponseDTO.getLastName());
        assertEquals(UserRole.USER, userResponseDTO.getRole());
        assertNotNull(userResponseDTO.getRegisterDate());
    }

    @Test
    @DisplayName("Should update User entity with UserUpdateDTO")
    void shouldUpdateUserEntityWithUserUpdateDTO() {
        // Given
        User existingUser = User.builder()
                .userId(1L)
                .userName("olduser")
                .email("old@example.com")
                .password("oldpassword")
                .name("Old")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .userName("newuser")
                .email("new@example.com")
                .name("New")
                .build();

        // When
        userMapper.updateEntity(userUpdateDTO, existingUser);

        // Then
        assertEquals(1L, existingUser.getUserId()); // Should remain unchanged
        assertEquals("newuser", existingUser.getUserName());
        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals("oldpassword", existingUser.getPassword()); // Should remain unchanged
        assertEquals("New", existingUser.getName());
        assertEquals("User", existingUser.getLastName()); // Should remain unchanged
        assertEquals(UserRole.USER, existingUser.getRole()); // Should remain unchanged
        assertNotNull(existingUser.getRegisterDate()); // Should remain unchanged
    }

    @Test
    @DisplayName("Should convert UserUpdateDTO to User entity")
    void shouldConvertUserUpdateDTOToUserEntity() {
        // Given
        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build();

        // When
        User user = userMapper.toEntity(userUpdateDTO);

        // Then
        assertNotNull(user);
        assertNull(user.getUserId()); // Should be ignored
        assertEquals("testuser", user.getUserName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Test", user.getName());
        assertEquals("User", user.getLastName());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertNull(user.getRegisterDate()); // Should be ignored
    }

    @Test
    @DisplayName("Should handle null values in UserUpdateDTO")
    void shouldHandleNullValuesInUserUpdateDTO() {
        // Given
        User existingUser = User.builder()
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .userName(null) // Null value
                .email("new@example.com")
                .build();

        // When
        userMapper.updateEntity(userUpdateDTO, existingUser);

        // Then
        assertEquals("testuser", existingUser.getUserName()); // Should remain unchanged (null ignored)
        assertEquals("new@example.com", existingUser.getEmail()); // Should be updated
        assertEquals("password123", existingUser.getPassword()); // Should remain unchanged
        assertEquals("Test", existingUser.getName()); // Should remain unchanged
        assertEquals("User", existingUser.getLastName()); // Should remain unchanged
        assertEquals(UserRole.USER, existingUser.getRole()); // Should remain unchanged
    }

    @Test
    @DisplayName("Should handle admin role correctly")
    void shouldHandleAdminRoleCorrectly() {
        // Given
        UserRequestDTO adminRequest = UserRequestDTO.builder()
                .userName("admin")
                .email("admin@example.com")
                .password("adminpass123")
                .name("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build();

        // When
        User adminUser = userMapper.toEntity(adminRequest);
        UserResponseDTO adminResponse = userMapper.toResponseDTO(adminUser);

        // Then
        assertEquals(UserRole.ADMIN, adminUser.getRole());
        assertEquals(UserRole.ADMIN, adminResponse.getRole());
    }

    @Test
    @DisplayName("Should exclude password from UserResponseDTO")
    void shouldExcludePasswordFromUserResponseDTO() {
        // Given
        User user = User.builder()
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .password("secretpassword")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        // When
        UserResponseDTO response = userMapper.toResponseDTO(user);

        // Then
        assertNotNull(response);
        // Password is not included in UserResponseDTO for security reasons
        assertEquals("testuser", response.getUserName());
        assertEquals("test@example.com", response.getEmail());
    }
}

