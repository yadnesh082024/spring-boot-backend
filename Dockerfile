# Use an official Gradle image to build the app
FROM gradle:7.5.1-jdk17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy only the necessary files
COPY build.gradle settings.gradle gradle.properties ./
COPY src ./src

# Build the project and generate the JAR file
RUN gradle clean build -x test

# Use a minimal JDK image to run the application
FROM openjdk:17-jdk-alpine

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set the working directory for the runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership of the app directory
RUN chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]