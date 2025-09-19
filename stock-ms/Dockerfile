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

WORKDIR /app

# Copiar archivos Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x ./mvnw

# Copiar código fuente
COPY src ./src

# Construir
RUN ./mvnw clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
