# ======================
# 1. Build stage
# ======================
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy pom.xml and download dependencies first (layer caching optimization)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application (skip tests to speed up build)
RUN mvn clean package -DskipTests


# ======================
# 2. Runtime stage
# ======================
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the Spring Boot app port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
