# Stage 1: Build stage
FROM eclipse-temurin:17-jdk-alpine as build

# Set the working directory in the container
WORKDIR /app

# Copy Gradle wrapper and dependencies files to leverage Docker caching
COPY gradle ./gradle
COPY gradlew .
COPY build.gradle settings.gradle ./

# Grant execute permission to gradlew
RUN chmod +x ./gradlew

# Download dependencies before copying the source code to speed up build caching
RUN ./gradlew --no-daemon build -x test || return 0

# Now copy the rest of the project
COPY src ./src

# Build the project and generate the JAR file (only build if needed)
RUN ./gradlew --no-daemon clean build -x test

# Stage 2: Production stage - use a minimal JRE or distroless image
FROM gcr.io/distroless/java17-debian11 as production

# Create a non-root user and group
USER 1001

# Set the working directory for the runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]