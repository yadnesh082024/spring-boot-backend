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
RUN ./gradlew clean build -x test --no-daemon || return 0

# Now copy the rest of the project
COPY src ./src

# Build the project and generate the JAR file
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Production stage
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set the working directory for the runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Change ownership of the app directory and switch to the non-root user
RUN chown -R appuser:appgroup /app && \
    chmod +x /app/app.jar

USER appuser

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]