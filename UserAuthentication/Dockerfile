FROM ubuntu:latest
LABEL authors="davidsaffer"

ENTRYPOINT ["top", "-b"]

# Use the official Amazon Corretto 17 image as the base image for building the application
FROM amazoncorretto:17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy the project source
COPY src src

# Build the application
RUN ./mvnw install -DskipTests

# Use Amazon Corretto 17 for the runtime image
FROM amazoncorretto:17

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/app.jar"]
