package com.microservices.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserRole enum using JUnit 5
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserRole Enum Tests")
class UserRoleTest {

    @Test
    @DisplayName("Should have correct enum values")
    void shouldHaveCorrectEnumValues() {
        // Given & When & Then
        UserRole[] roles = UserRole.values();
        
        assertEquals(2, roles.length);
        assertEquals(UserRole.ADMIN, roles[0]);
        assertEquals(UserRole.USER, roles[1]);
    }

    @Test
    @DisplayName("Should get correct display names")
    void shouldGetCorrectDisplayNames() {
        // Given & When & Then
        assertEquals("Administrador", UserRole.ADMIN.getDisplayName());
        assertEquals("Usuario", UserRole.USER.getDisplayName());
    }

    @Test
    @DisplayName("Should correctly identify admin role")
    void shouldCorrectlyIdentifyAdminRole() {
        // Given & When & Then
        assertTrue(UserRole.ADMIN.isAdmin());
        assertFalse(UserRole.USER.isAdmin());
    }

    @Test
    @DisplayName("Should correctly identify user role")
    void shouldCorrectlyIdentifyUserRole() {
        // Given & When & Then
        assertTrue(UserRole.USER.isUser());
        assertFalse(UserRole.ADMIN.isUser());
    }

    @Test
    @DisplayName("Should use ternary operators correctly for admin check")
    void shouldUseTernaryOperatorsCorrectlyForAdminCheck() {
        // Given
        UserRole adminRole = UserRole.ADMIN;
        UserRole userRole = UserRole.USER;

        // When & Then
        assertTrue(adminRole == UserRole.ADMIN ? true : false);
        assertFalse(userRole == UserRole.ADMIN ? true : false);
    }

    @Test
    @DisplayName("Should use ternary operators correctly for user check")
    void shouldUseTernaryOperatorsCorrectlyForUserCheck() {
        // Given
        UserRole adminRole = UserRole.ADMIN;
        UserRole userRole = UserRole.USER;

        // When & Then
        assertTrue(userRole == UserRole.USER ? true : false);
        assertFalse(adminRole == UserRole.USER ? true : false);
    }

    @Test
    @DisplayName("Should have non-null display names")
    void shouldHaveNonNullDisplayNames() {
        // Given & When & Then
        assertNotNull(UserRole.ADMIN.getDisplayName());
        assertNotNull(UserRole.USER.getDisplayName());
        assertFalse(UserRole.ADMIN.getDisplayName().isEmpty());
        assertFalse(UserRole.USER.getDisplayName().isEmpty());
    }

    @Test
    @DisplayName("Should have unique display names")
    void shouldHaveUniqueDisplayNames() {
        // Given & When & Then
        assertNotEquals(UserRole.ADMIN.getDisplayName(), UserRole.USER.getDisplayName());
    }

    @Test
    @DisplayName("Should work with switch statements")
    void shouldWorkWithSwitchStatements() {
        // Given
        UserRole adminRole = UserRole.ADMIN;
        UserRole userRole = UserRole.USER;

        // When & Then
        String adminResult = switch (adminRole) {
            case ADMIN -> "Admin Role";
            case USER -> "User Role";
        };

        String userResult = switch (userRole) {
            case ADMIN -> "Admin Role";
            case USER -> "User Role";
        };

        assertEquals("Admin Role", adminResult);
        assertEquals("User Role", userResult);
    }

    @Test
    @DisplayName("Should work with valueOf method")
    void shouldWorkWithValueOfMethod() {
        // Given & When & Then
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.USER, UserRole.valueOf("USER"));
    }

    @Test
    @DisplayName("Should throw exception for invalid enum value")
    void shouldThrowExceptionForInvalidEnumValue() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> UserRole.valueOf("INVALID_ROLE"));
    }

    @Test
    @DisplayName("Should have consistent toString behavior")
    void shouldHaveConsistentToStringBehavior() {
        // Given & When & Then
        assertEquals("ADMIN", UserRole.ADMIN.toString());
        assertEquals("USER", UserRole.USER.toString());
    }

    @Test
    @DisplayName("Should work with streams and collections")
    void shouldWorkWithStreamsAndCollections() {
        // Given & When
        long adminCount = java.util.Arrays.stream(UserRole.values())
                .filter(UserRole::isAdmin)
                .count();

        long userCount = java.util.Arrays.stream(UserRole.values())
                .filter(UserRole::isUser)
                .count();

        // Then
        assertEquals(1, adminCount);
        assertEquals(1, userCount);
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        // Given
        String originalAdminDisplayName = UserRole.ADMIN.getDisplayName();
        String originalUserDisplayName = UserRole.USER.getDisplayName();

        // When - Try to access multiple times
        String adminDisplayName1 = UserRole.ADMIN.getDisplayName();
        String adminDisplayName2 = UserRole.ADMIN.getDisplayName();
        String userDisplayName1 = UserRole.USER.getDisplayName();
        String userDisplayName2 = UserRole.USER.getDisplayName();

        // Then
        assertEquals(originalAdminDisplayName, adminDisplayName1);
        assertEquals(originalAdminDisplayName, adminDisplayName2);
        assertEquals(originalUserDisplayName, userDisplayName1);
        assertEquals(originalUserDisplayName, userDisplayName2);
    }
}
