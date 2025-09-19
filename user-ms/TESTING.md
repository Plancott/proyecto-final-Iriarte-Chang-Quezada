# 🧪 Guía de Testing para User-MS

## 📋 Resumen

Este documento explica cómo ejecutar y entender los tests del microservicio `user-ms`. Hemos implementado una estrategia completa de testing que incluye tests unitarios, de integración y de seguridad.

## 🏗️ Estructura de Tests

### Tests Unitarios
- **AuthorizationServiceTest**: Tests de lógica de autorización
- **JwtServiceTest**: Tests de generación y validación de JWT
- **GlobalExceptionHandlerTest**: Tests de manejo de excepciones
- **UserServiceTest**: Tests de lógica de negocio (mejorado)

### Tests de Integración
- **UserControllerIntegrationTest**: Tests de flujos completos
- **AuthClientIntegrationTest**: Tests de comunicación con auth-ms

### Utilidades
- **TestDataBuilder**: Builder pattern para datos de prueba
- **TestConfig**: Configuración específica para tests

## 🚀 Cómo Ejecutar los Tests

### Opción 1: Script Automático (Recomendado)
```bash
# En Windows
run-tests.bat

# En Linux/Mac
chmod +x run-tests.sh
./run-tests.sh
```

### Opción 2: Maven Manual
```bash
# Todos los tests
mvn test

# Solo tests unitarios
mvn test -Dtest="*Test"

# Solo tests de integración
mvn test -Dtest="*IntegrationTest"

# Con reporte de cobertura
mvn test jacoco:report
```

### Opción 3: IDE
- **IntelliJ IDEA**: Click derecho en `src/test` → "Run All Tests"
- **Eclipse**: Click derecho en proyecto → "Run As" → "Maven Test"
- **VS Code**: Usar extensiones de Java Testing

## 📊 Cobertura de Código

### Objetivos de Cobertura
- **Unit Tests**: 90%+ cobertura
- **Integration Tests**: 80%+ cobertura
- **Security Tests**: 100% cobertura de flujos críticos

### Verificar Cobertura
```bash
mvn jacoco:report
# Abrir target/site/jacoco/index.html en el navegador
```

## 🔧 Configuración de Tests

### Perfiles de Test
- **test**: Perfil principal para tests
- **integration-test**: Para tests de integración con servicios externos

### Base de Datos de Test
- **H2 en memoria**: Configurada automáticamente
- **Datos limpios**: Se limpia antes de cada test
- **Transacciones**: Rollback automático

## 📝 Tipos de Tests Implementados

### 1. Tests de Autorización
```java
@Test
void shouldAllowAdminToAccessAnyResource() {
    // Test de permisos de administrador
}
```

### 2. Tests de JWT
```java
@Test
void shouldGenerateTokenForUserSuccessfully() {
    // Test de generación de tokens
}
```

### 3. Tests de Excepciones
```java
@Test
void shouldHandleUserNotFoundExceptionCorrectly() {
    // Test de manejo de errores
}
```

### 4. Tests de Integración
```java
@Test
void shouldCompleteFullUserRegistrationAndLoginFlow() {
    // Test de flujo completo
}
```

## 🐛 Debugging de Tests

### Logs de Test
```bash
# Ejecutar con logs detallados
mvn test -Dlogging.level.com.microservices=DEBUG
```

### Tests Fallidos
```bash
# Ejecutar solo tests fallidos
mvn test -Dtest="*Test" -DfailIfNoTests=false
```

### Base de Datos de Test
```bash
# Ver datos de test en H2 Console
# URL: http://localhost:8081/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Usuario: sa
# Contraseña: (vacía)
```

## 📈 Mejores Prácticas Implementadas

### 1. Naming Conventions
- **Métodos**: `should[ExpectedBehavior]When[Condition]`
- **Clases**: `[ComponentName]Test`
- **Archivos**: `*Test.java`, `*IntegrationTest.java`

### 2. Estructura AAA
```java
@Test
void shouldDoSomething() {
    // Arrange - Configurar datos de prueba
    User user = TestDataBuilder.user().build();
    
    // Act - Ejecutar la acción
    UserResponseDTO result = userService.createUser(user);
    
    // Assert - Verificar resultados
    assertNotNull(result);
    assertEquals("testuser", result.getUserName());
}
```

### 3. Mocking Estratégico
- **Servicios externos**: Siempre mockeados
- **Base de datos**: H2 en memoria
- **Dependencias**: Mockeadas en tests unitarios

### 4. Datos de Prueba
- **TestDataBuilder**: Patrón builder para datos consistentes
- **Datos realistas**: Nombres, emails, etc. realistas
- **Casos edge**: Datos límite y casos especiales

## 🔍 Troubleshooting

### Problemas Comunes

#### 1. Tests Fallan por Base de Datos
```bash
# Limpiar base de datos
mvn clean test
```

#### 2. Tests de Integración Fallan
```bash
# Verificar que auth-ms esté ejecutándose
# O ejecutar solo tests unitarios
mvn test -Dtest="*Test"
```

#### 3. Problemas de Dependencias
```bash
# Limpiar y reinstalar
mvn clean install
```

### Logs Útiles
```bash
# Ver logs de Spring
mvn test -Dlogging.level.org.springframework=DEBUG

# Ver logs de Hibernate
mvn test -Dlogging.level.org.hibernate=DEBUG
```

## 📚 Recursos Adicionales

### Documentación
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

### Herramientas
- **Maven Surefire**: Ejecución de tests
- **JaCoCo**: Cobertura de código
- **H2 Database**: Base de datos de test
- **Mockito**: Mocking de dependencias

## 🎯 Próximos Pasos

### Mejoras Futuras
1. **Tests de Performance**: Carga y rendimiento
2. **Tests de Contrato**: Para APIs externas
3. **Tests de Seguridad**: Penetration testing
4. **Tests de UI**: Si se agrega frontend

### Mantenimiento
1. **Actualizar tests** cuando se cambie la lógica
2. **Revisar cobertura** en cada release
3. **Optimizar tiempo** de ejecución de tests
4. **Documentar** nuevos casos de prueba

---

**¡Happy Testing! 🚀**
