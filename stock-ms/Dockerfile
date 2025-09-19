## Etapa 1: build (opcional si quieres usar multi-stage)
#FROM eclipse-temurin:21-jdk AS builder
#WORKDIR /app
#COPY . .
#RUN ./mvnw clean package -DskipTests
#
## Etapa 2: imagen final
#FROM eclipse-temurin:21-jdk-alpine
#WORKDIR /app
#COPY --from=builder /app/target/stock-ms-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# ==========================
# Etapa 1: Build
# ==========================
FROM eclipse-temurin:21-jdk-alpine AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Dar permisos al wrapper (soluciona "Permission denied")
RUN chmod +x mvnw

# Copiar el resto del proyecto
COPY src ./src

# Ejecutar build de Maven (con skip tests)
RUN ./mvnw clean package -DskipTests

# ==========================
# Etapa 2: Imagen final
# ==========================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar el jar generado de la etapa builder
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java","-jar","app.jar"]
