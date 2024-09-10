# Use an official Temurin JRE runtime as a parent image
FROM eclipse-temurin:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the GitHub Actions build
COPY build/libs/github-actions-backend.jar /app/github-actions-backend.jar

# Expose the port the app runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/github-actions-backend.jar"]
