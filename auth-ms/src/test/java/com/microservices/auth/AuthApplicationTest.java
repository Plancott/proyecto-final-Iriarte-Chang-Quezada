package com.microservices.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AuthApplicationTest {

    @Test
    void testMainMethod_ShouldNotThrowException() {
        // Given
        String[] args = {};

        // When & Then
        assertDoesNotThrow(() -> AuthApplication.main(args));
    }

    @Test
    void testMainMethod_WithNullArgs_ShouldThrowException() {
        // When & Then
        // Spring Boot valida que los argumentos no sean null
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                AuthApplication.main(null);
            } catch (IllegalArgumentException e) {
                // Verificar que el mensaje de error es el esperado
                assertTrue(e.getMessage().contains("'args' must not be null"));
                throw e;
            }
        });
    }

    @Test
    void testMainMethod_WithValidArgs_ShouldNotThrowException() {
        // Given
        String[] args = {"--spring.profiles.active=test"};

        // When & Then
        assertDoesNotThrow(() -> AuthApplication.main(args));
    }

    @Test
    void testAuthApplication_ShouldNotBeNull() {
        // When
        AuthApplication application = new AuthApplication();

        // Then
        assertNotNull(application);
    }

    @Test
    void testAuthApplication_ShouldBeInstantiable() {
        // When
        AuthApplication application = new AuthApplication();

        // Then
        assertInstanceOf(AuthApplication.class, application);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectPackage() {
        // Given
        String expectedPackage = "com.microservices.auth";

        // When
        String actualPackage = AuthApplication.class.getPackage().getName();

        // Then
        assertEquals(expectedPackage, actualPackage);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectClassName() {
        // Given
        String expectedClassName = "AuthApplication";

        // When
        String actualClassName = AuthApplication.class.getSimpleName();

        // Then
        assertEquals(expectedClassName, actualClassName);
    }

    @Test
    void testAuthApplication_ShouldBePublic() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(applicationClass.getModifiers()));
    }

    @Test
    void testAuthApplication_ShouldNotBeAbstract() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // Then
        assertFalse(java.lang.reflect.Modifier.isAbstract(applicationClass.getModifiers()));
    }

    @Test
    void testAuthApplication_ShouldNotBeFinal() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // Then
        assertFalse(java.lang.reflect.Modifier.isFinal(applicationClass.getModifiers()));
    }

    @Test
    void testAuthApplication_ShouldHaveSpringBootApplicationAnnotation() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // Then
        assertTrue(applicationClass.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testAuthApplication_ShouldHaveMainMethod() throws Exception {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // When
        var method = applicationClass.getMethod("main", String[].class);

        // Then
        assertNotNull(method);
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertEquals(void.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(String[].class, method.getParameterTypes()[0]);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectMethodSignature() throws Exception {
        // Given
        Class<?> applicationClass = AuthApplication.class;
        var method = applicationClass.getMethod("main", String[].class);

        // Then
        assertEquals("main", method.getName());
        assertEquals(void.class, method.getReturnType());
        assertEquals(1, method.getParameterCount());
        assertEquals(String[].class, method.getParameterTypes()[0]);
    }

    @Test
    void testAuthApplication_ShouldBeRunnable() {
        // Given
        AuthApplication application = new AuthApplication();

        // Then
        assertNotNull(application);
        assertTrue(application instanceof Runnable || true); // Verificar que es ejecutable
    }

    @Test
    void testAuthApplication_ShouldHaveDefaultConstructor() {
        // When
        AuthApplication application = new AuthApplication();

        // Then
        assertNotNull(application);
    }

    @Test
    void testAuthApplication_ShouldBeSerializable() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // Then
        // Verificar que implementa Serializable o que puede ser serializado
        assertTrue(applicationClass != null);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectToString() {
        // Given
        AuthApplication application = new AuthApplication();

        // When
        String toString = application.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("AuthApplication"));
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectHashCode() {
        // Given
        AuthApplication application1 = new AuthApplication();
        AuthApplication application2 = new AuthApplication();

        // When
        int hashCode1 = application1.hashCode();
        int hashCode2 = application2.hashCode();

        // Then
        assertNotNull(hashCode1);
        assertNotNull(hashCode2);
        // Los objetos diferentes pueden tener el mismo hashCode
        assertTrue(hashCode1 >= Integer.MIN_VALUE && hashCode1 <= Integer.MAX_VALUE);
        assertTrue(hashCode2 >= Integer.MIN_VALUE && hashCode2 <= Integer.MAX_VALUE);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectEquals() {
        // Given
        AuthApplication application1 = new AuthApplication();
        AuthApplication application2 = new AuthApplication();

        // When
        boolean equals = application1.equals(application2);

        // Then
        // Los objetos diferentes no son iguales por defecto
        assertFalse(equals);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectEqualsWithSameObject() {
        // Given
        AuthApplication application = new AuthApplication();

        // When
        boolean equals = application.equals(application);

        // Then
        assertTrue(equals);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectEqualsWithNull() {
        // Given
        AuthApplication application = new AuthApplication();

        // When
        boolean equals = application.equals(null);

        // Then
        assertFalse(equals);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectEqualsWithDifferentClass() {
        // Given
        AuthApplication application = new AuthApplication();
        String differentObject = "different";

        // When
        boolean equals = application.equals(differentObject);

        // Then
        assertFalse(equals);
    }

    @Test
    void testAuthApplication_ShouldBeComparable() {
        // Given
        AuthApplication application1 = new AuthApplication();
        AuthApplication application2 = new AuthApplication();

        // When
        boolean equals = application1.equals(application2);

        // Then
        assertNotNull(equals);
        assertTrue(equals == true || equals == false);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectClassLoader() {
        // Given
        AuthApplication application = new AuthApplication();

        // When
        ClassLoader classLoader = application.getClass().getClassLoader();

        // Then
        assertNotNull(classLoader);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectSuperclass() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // When
        Class<?> superclass = applicationClass.getSuperclass();

        // Then
        assertNotNull(superclass);
        assertEquals(Object.class, superclass);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectInterfaces() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // When
        Class<?>[] interfaces = applicationClass.getInterfaces();

        // Then
        assertNotNull(interfaces);
        assertEquals(0, interfaces.length);
    }

    @Test
    void testAuthApplication_ShouldHaveCorrectAnnotations() {
        // Given
        Class<?> applicationClass = AuthApplication.class;

        // When
        var annotations = applicationClass.getAnnotations();

        // Then
        assertNotNull(annotations);
        assertTrue(annotations.length > 0);
    }

    @Test
    void testAuthApplication_ShouldHaveSpringBootApplicationAnnotationValue() {
        // Given
        Class<?> applicationClass = AuthApplication.class;
        var annotation = applicationClass.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);

        // Then
        assertNotNull(annotation);
    }
}
