# Use Amazon Corretto JDK to build and run the Spring Boot application
FROM amazoncorretto:17 as build

# Add Maintainer Info
LABEL maintainer="davidsaffer"

# Set the working directory in the Docker image
WORKDIR /app

# Copy maven executable and POM file to the image
COPY mvnw pom.xml ./
COPY .mvn .mvn

# First download dependencies so they can be cached.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src ./src

# Build the application without running tests
RUN ./mvnw package -DskipTests

# For the final image, use Amazon Corretto Runtime to reduce the image size
FROM amazoncorretto:17

# Add Maintainer Info
LABEL maintainer="davidsaffer"

# Add a volume pointing to "/tmp" - This is useful for Tomcat by default
VOLUME /tmp

# Expose the port the application runs on
EXPOSE 8080

# Set the working directory and copy over the built artifact from the previous stage
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar ./app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
