# Use an official temurin JDK runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle settings.gradle /app/

# Copy the Gradle wrapper files
COPY gradlew /app/
COPY gradle /app/gradle

# Download the dependencies
RUN ./gradlew build --no-daemon || return 0

# Copy the project files
COPY . /app

# Build the application
RUN ./gradlew build --no-daemon

# Expose the port the app runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "build/libs/github-actions-backend.jar"]