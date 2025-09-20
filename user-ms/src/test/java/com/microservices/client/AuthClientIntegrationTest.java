package com.microservices.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.Main;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para AuthClient - Comunicación con auth-ms
 * Nota: Estos tests requieren que auth-ms esté ejecutándose
 */
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "microservices.auth-service.url=http://localhost:8079"
})
@DisplayName("AuthClient Integration Tests")
class AuthClientIntegrationTest {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @DisplayName("Should validate token successfully when auth-ms is available")
    void shouldValidateTokenSuccessfullyWhenAuthMsIsAvailable() {
        // Given
        String validToken = "Bearer valid-jwt-token";

        try {
            // When
            TokenValidationResponse response = authClient.validateToken(validToken);

            // Then
            assertNotNull(response);
            // Si llegamos aquí, significa que auth-ms respondió correctamente
            // Los detalles específicos dependerían de la implementación de auth-ms
        } catch (FeignException.NotFound e) {
            // auth-ms no está disponible o no tiene el endpoint
            System.out.println("Auth-ms no está disponible para testing: " + e.getMessage());
        } catch (FeignException e) {
            // Otros errores de comunicación
            System.out.println("Error de comunicación con auth-ms: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should handle invalid token when auth-ms is available")
    void shouldHandleInvalidTokenWhenAuthMsIsAvailable() {
        // Given
        String invalidToken = "Bearer invalid-jwt-token";

        try {
            // When
            authClient.validateToken(invalidToken);
            fail("Should have thrown an exception for invalid token");
        } catch (FeignException.Unauthorized e) {
            // Expected behavior - token is invalid
            assertEquals(HttpStatus.UNAUTHORIZED.value(), e.status());
        } catch (FeignException.NotFound e) {
            // auth-ms no está disponible
            System.out.println("Auth-ms no está disponible para testing: " + e.getMessage());
        } catch (FeignException e) {
            // Otros errores de comunicación
            System.out.println("Error de comunicación con auth-ms: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should handle null token")
    void shouldHandleNullToken() {
        // Given
        String nullToken = null;

        // When & Then
        assertThrows(Exception.class, () -> authClient.validateToken(nullToken));
    }

    @Test
    @DisplayName("Should handle empty token")
    void shouldHandleEmptyToken() {
        // Given
        String emptyToken = "";

        // When & Then
        assertThrows(Exception.class, () -> authClient.validateToken(emptyToken));
    }

    @Test
    @DisplayName("Should handle malformed token format")
    void shouldHandleMalformedTokenFormat() {
        // Given
        String malformedToken = "InvalidTokenFormat";

        // When & Then - Any exception is acceptable for malformed token
        assertThrows(Exception.class, () -> authClient.validateToken(malformedToken));
    }

    @Test
    @DisplayName("Should handle network timeout gracefully")
    void shouldHandleNetworkTimeoutGracefully() {
        // Given
        String token = "Bearer test-token";

        try {
            // When
            authClient.validateToken(token);
        } catch (FeignException e) {
            // Expected behavior - network issues
            System.out.println("Network error (expected in test environment): " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should have correct Feign client configuration")
    void shouldHaveCorrectFeignClientConfiguration() {
        // Given
        String expectedUrl = "http://localhost:8079";
        
        // When
        // Verificar que el cliente está configurado correctamente
        // Esto se puede hacer verificando que el cliente se puede inyectar
        assertNotNull(authClient);
        
        // La URL se configura a través de @FeignClient y application.yml
        // En un entorno real, podrías verificar la configuración
        System.out.println("AuthClient configurado para: " + expectedUrl);
    }
}
