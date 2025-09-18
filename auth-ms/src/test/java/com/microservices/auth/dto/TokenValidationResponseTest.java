package com.microservices.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenValidationResponseTest {

    @Test
    void testDefaultConstructor_ShouldCreateEmptyObject() {
        // When
        TokenValidationResponse response = new TokenValidationResponse();

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testAllArgsConstructor_ShouldSetAllFields() {
        // Given
        boolean valid = true;
        String userName = "testuser";
        String role = "USER";
        Long userId = 123L;

        // When
        TokenValidationResponse response = new TokenValidationResponse(valid, userName, role, userId);

        // Then
        assertNotNull(response);
        assertEquals(valid, response.isValid());
        assertEquals(userName, response.getUserName());
        assertEquals(role, response.getRole());
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testAllArgsConstructor_WithNullValues_ShouldSetNullFields() {
        // Given
        boolean valid = false;
        String userName = null;
        String role = null;
        Long userId = null;

        // When
        TokenValidationResponse response = new TokenValidationResponse(valid, userName, role, userId);

        // Then
        assertNotNull(response);
        assertEquals(valid, response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testBuilder_ShouldCreateObjectWithBuilder() {
        // When
        TokenValidationResponse response = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals("testuser", response.getUserName());
        assertEquals("USER", response.getRole());
        assertEquals(123L, response.getUserId());
    }

    @Test
    void testBuilder_WithPartialData_ShouldCreateObjectWithPartialData() {
        // When
        TokenValidationResponse response = TokenValidationResponse.builder()
                .valid(false)
                .userName("testuser")
                .build();

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals("testuser", response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testBuilder_WithEmptyBuilder_ShouldCreateEmptyObject() {
        // When
        TokenValidationResponse response = TokenValidationResponse.builder().build();

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testSetters_ShouldSetValuesCorrectly() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();

        // When
        response.setValid(true);
        response.setUserName("testuser");
        response.setRole("USER");
        response.setUserId(123L);

        // Then
        assertTrue(response.isValid());
        assertEquals("testuser", response.getUserName());
        assertEquals("USER", response.getRole());
        assertEquals(123L, response.getUserId());
    }

    @Test
    void testSetters_WithNullValues_ShouldSetNullValues() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        response.setValid(true);
        response.setUserName("testuser");
        response.setRole("USER");
        response.setUserId(123L);

        // When
        response.setValid(false);
        response.setUserName(null);
        response.setRole(null);
        response.setUserId(null);

        // Then
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testEquals_WithSameValues_ShouldReturnTrue() {
        // Given
        TokenValidationResponse response1 = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        TokenValidationResponse response2 = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        // When & Then
        assertEquals(response1, response2);
    }

    @Test
    void testEquals_WithDifferentValues_ShouldReturnFalse() {
        // Given
        TokenValidationResponse response1 = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        TokenValidationResponse response2 = TokenValidationResponse.builder()
                .valid(false)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        // When & Then
        assertNotEquals(response1, response2);
    }

    @Test
    void testEquals_WithNullValues_ShouldReturnTrue() {
        // Given
        TokenValidationResponse response1 = new TokenValidationResponse();
        TokenValidationResponse response2 = new TokenValidationResponse();

        // When & Then
        assertEquals(response1, response2);
    }

    @Test
    void testHashCode_WithSameValues_ShouldReturnSameHashCode() {
        // Given
        TokenValidationResponse response1 = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        TokenValidationResponse response2 = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        // When & Then
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testHashCode_WithDifferentValues_ShouldReturnDifferentHashCode() {
        // Given
        TokenValidationResponse response1 = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        TokenValidationResponse response2 = TokenValidationResponse.builder()
                .valid(false)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        // When & Then
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString_ShouldContainAllFields() {
        // Given
        TokenValidationResponse response = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("valid=true"));
        assertTrue(toString.contains("userName=testuser"));
        assertTrue(toString.contains("role=USER"));
        assertTrue(toString.contains("userId=123"));
    }

    @Test
    void testToString_WithNullValues_ShouldContainNullValues() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("valid=false"));
        assertTrue(toString.contains("userName=null"));
        assertTrue(toString.contains("role=null"));
        assertTrue(toString.contains("userId=null"));
    }

    @Test
    void testValidField_WithTrueValue_ShouldReturnTrue() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();

        // When
        response.setValid(true);

        // Then
        assertTrue(response.isValid());
    }

    @Test
    void testValidField_WithFalseValue_ShouldReturnFalse() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();

        // When
        response.setValid(false);

        // Then
        assertFalse(response.isValid());
    }

    @Test
    void testUserNameField_WithValidString_ShouldReturnString() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        String userName = "testuser";

        // When
        response.setUserName(userName);

        // Then
        assertEquals(userName, response.getUserName());
    }

    @Test
    void testUserNameField_WithEmptyString_ShouldReturnEmptyString() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        String userName = "";

        // When
        response.setUserName(userName);

        // Then
        assertEquals(userName, response.getUserName());
    }

    @Test
    void testRoleField_WithValidString_ShouldReturnString() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        String role = "ADMIN";

        // When
        response.setRole(role);

        // Then
        assertEquals(role, response.getRole());
    }

    @Test
    void testRoleField_WithEmptyString_ShouldReturnEmptyString() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        String role = "";

        // When
        response.setRole(role);

        // Then
        assertEquals(role, response.getRole());
    }

    @Test
    void testUserIdField_WithValidLong_ShouldReturnLong() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        Long userId = 123L;

        // When
        response.setUserId(userId);

        // Then
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testUserIdField_WithZeroValue_ShouldReturnZero() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        Long userId = 0L;

        // When
        response.setUserId(userId);

        // Then
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testUserIdField_WithNegativeValue_ShouldReturnNegativeValue() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        Long userId = -1L;

        // When
        response.setUserId(userId);

        // Then
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testUserIdField_WithMaxValue_ShouldReturnMaxValue() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        Long userId = Long.MAX_VALUE;

        // When
        response.setUserId(userId);

        // Then
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testUserIdField_WithMinValue_ShouldReturnMinValue() {
        // Given
        TokenValidationResponse response = new TokenValidationResponse();
        Long userId = Long.MIN_VALUE;

        // When
        response.setUserId(userId);

        // Then
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testBuilder_WithSpecialCharacters_ShouldHandleCorrectly() {
        // Given
        String userName = "user@domain.com";
        String role = "ROLE_ADMIN";

        // When
        TokenValidationResponse response = TokenValidationResponse.builder()
                .valid(true)
                .userName(userName)
                .role(role)
                .userId(456L)
                .build();

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(userName, response.getUserName());
        assertEquals(role, response.getRole());
        assertEquals(456L, response.getUserId());
    }

    @Test
    void testBuilder_WithLongStrings_ShouldHandleCorrectly() {
        // Given
        String longUserName = "verylongusernamethatexceedsnormallength";
        String longRole = "VERY_LONG_ROLE_NAME_THAT_EXCEEDS_NORMAL_LENGTH";

        // When
        TokenValidationResponse response = TokenValidationResponse.builder()
                .valid(true)
                .userName(longUserName)
                .role(longRole)
                .userId(789L)
                .build();

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(longUserName, response.getUserName());
        assertEquals(longRole, response.getRole());
        assertEquals(789L, response.getUserId());
    }
}
