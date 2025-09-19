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

# Copiar archivos de Maven del módulo stock-ms
COPY stock-ms/pom.xml .
COPY stock-ms/.mvn .mvn
COPY stock-ms/mvnw .

RUN chmod +x mvnw

# Copiar código fuente de stock-ms
COPY stock-ms/src ./src

# Construir el proyecto
RUN ./mvnw clean package -DskipTests

# ==========================
# Etapa final
# ==========================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar el jar generado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
