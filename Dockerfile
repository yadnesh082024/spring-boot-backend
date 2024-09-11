# Use an official Gradle image to build the app
FROM gradle:7.5.1-jdk17 as build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle files and project source code to the container
COPY . .

# Build the project and generate the JAR file
RUN gradle clean build -x test

# Use a minimal JDK image to run the application
FROM openjdk:17-jdk-alpine

# Set the working directory for the runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

