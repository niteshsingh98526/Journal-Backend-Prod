# ======================
# 1. Build stage
# ======================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the Spring Boot JAR (skip tests for faster builds)
RUN mvn clean package -DskipTests


# ======================
# 2. Runtime stage
# ======================
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the Spring Boot app port
EXPOSE 8080

# Use env variables (better for deployment)
ENV SPRING_PROFILES_ACTIVE=docker

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
