# Use an official OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /shortly-docker-app

# This expects a jar here, need to build the project using mvn clean install -DskipTests
# Copy your built jar file into the container
COPY target/Shortly-0.0.1-SNAPSHOT.jar .

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/shortly-docker-app/Shortly-0.0.1-SNAPSHOT.jar"]