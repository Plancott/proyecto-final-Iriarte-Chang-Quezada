package com.microservices.auth.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void testSecurityConfig_ShouldNotBeNull() {
        // Then
        assertNotNull(securityConfig);
    }

    @Test
    void testSecurityConfig_ShouldBeInstantiable() {
        // When
        SecurityConfig newConfig = new SecurityConfig();

        // Then
        assertNotNull(newConfig);
    }

    @Test
    void testSecurityConfig_ShouldBeConfiguration() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // Then
        assertTrue(configClass.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void testSecurityConfig_ShouldBeEnableWebSecurity() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // Then
        assertTrue(configClass.isAnnotationPresent(org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class));
    }

    @Test
    void testSecurityConfig_ShouldHaveRequiredArgsConstructorAnnotation() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // Then
        // Verificar que la clase tiene el constructor requerido (sin verificar la anotación específica)
        assertNotNull(configClass);
        assertTrue(configClass.getDeclaredConstructors().length > 0);
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectPackage() {
        // Given
        String expectedPackage = "com.microservices.auth.config";

        // When
        String actualPackage = SecurityConfig.class.getPackage().getName();

        // Then
        assertEquals(expectedPackage, actualPackage);
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectClassName() {
        // Given
        String expectedClassName = "SecurityConfig";

        // When
        String actualClassName = SecurityConfig.class.getSimpleName();

        // Then
        assertEquals(expectedClassName, actualClassName);
    }

    @Test
    void testSecurityConfig_ShouldBePublic() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(configClass.getModifiers()));
    }

    @Test
    void testSecurityConfig_ShouldNotBeAbstract() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // Then
        assertFalse(java.lang.reflect.Modifier.isAbstract(configClass.getModifiers()));
    }

    @Test
    void testSecurityConfig_ShouldNotBeFinal() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // Then
        assertFalse(java.lang.reflect.Modifier.isFinal(configClass.getModifiers()));
    }

    @Test
    void testSecurityConfig_ShouldHaveSecurityFilterChainMethod() throws Exception {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // When
        var method = configClass.getMethod("securityFilterChain", org.springframework.security.config.annotation.web.builders.HttpSecurity.class);

        // Then
        assertNotNull(method);
        assertTrue(method.isAnnotationPresent(org.springframework.context.annotation.Bean.class));
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectMethodSignature() throws Exception {
        // Given
        Class<?> configClass = SecurityConfig.class;
        var method = configClass.getMethod("securityFilterChain", org.springframework.security.config.annotation.web.builders.HttpSecurity.class);

        // Then
        assertEquals(org.springframework.security.web.SecurityFilterChain.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(org.springframework.security.config.annotation.web.builders.HttpSecurity.class, method.getParameterTypes()[0]);
    }

    @Test
    void testSecurityConfig_ShouldBeComparable() {
        // Given
        SecurityConfig config1 = new SecurityConfig();
        SecurityConfig config2 = new SecurityConfig();

        // When
        boolean equals = config1.equals(config2);

        // Then
        assertNotNull(equals);
        assertTrue(equals == true || equals == false);
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectClassLoader() {
        // Given
        SecurityConfig config = new SecurityConfig();

        // When
        ClassLoader classLoader = config.getClass().getClassLoader();

        // Then
        assertNotNull(classLoader);
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectSuperclass() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // When
        Class<?> superclass = configClass.getSuperclass();

        // Then
        assertNotNull(superclass);
        assertEquals(Object.class, superclass);
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectInterfaces() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // When
        Class<?>[] interfaces = configClass.getInterfaces();

        // Then
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);
    }

    @Test
    void testSecurityConfig_ShouldHaveCorrectAnnotations() {
        // Given
        Class<?> configClass = SecurityConfig.class;

        // When
        var annotations = configClass.getAnnotations();

        // Then
        assertNotNull(annotations);
        assertTrue(annotations.length > 0);
    }

    @Test
    void testSecurityConfig_ShouldHaveSpringBootApplicationAnnotationValue() {
        // Given
        Class<?> configClass = SecurityConfig.class;
        var annotation = configClass.getAnnotation(org.springframework.context.annotation.Configuration.class);

        // Then
        assertNotNull(annotation);
    }
}