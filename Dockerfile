# Etapa 1: build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copiar todos los archivos del proyecto (padre + módulos)
COPY stock-ms/pom.xml ./
COPY stock-ms/src ./src
COPY stock-ms/mvnw stock-ms/.mvn ./



# Etapa final
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar JAR construido del módulo hijo stock-ms
COPY --from=builder /app/stock-ms/target/stock-ms-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

