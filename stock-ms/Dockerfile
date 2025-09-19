# Etapa de construcción
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

# Dar permisos de ejecución a mvnw
RUN chmod +x ./mvnw

# Ejecutar build
RUN ./mvnw clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
