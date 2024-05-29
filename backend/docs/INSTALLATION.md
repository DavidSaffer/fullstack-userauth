# Installation Guide

This guide will walk you through the steps required to set up and run the User Authentication System, a Spring Boot application. The guide covers prerequisites and local setup.

## Prerequisites

Before you begin, ensure you have the following software installed on your machine:

1. **Java Development Kit (JDK)**:
    - JDK 17 (Amazon Corretto 17 recommended)
    - [Download JDK 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)

2. **Maven**:
    - Maven 3.6.3 or later
    - [Download Maven](https://maven.apache.org/download.cgi)

3. **MySQL**:
    - MySQL 8.0 or later
    - [Download MySQL](https://dev.mysql.com/downloads/mysql/)


## Step 1: Clone the Repository

First, clone the project repository from GitHub to your local machine:

```bash
git clone https://github.com/DavidSaffer/fullstack-userauth.git
cd backend
```

## Step 2: Configure the Database

1. **Start MySQL**:
    - Ensure your MySQL server is running. If not, start the MySQL service.

2. **Create Database**:
    - Create a new database for the application:
   ```sql
   CREATE DATABASE user_auth;
   ```

3. **Create User**:
    - Create a user and grant privileges:
   ```sql
   CREATE USER 'devuser'@'localhost' IDENTIFIED BY 'devpassword';
   GRANT ALL PRIVILEGES ON user_auth.* TO 'devuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

## Step 3: Configure Application Properties

Open the `application.properties` file located in `src/main/resources` and configure the database connection details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_auth
spring.datasource.username=${SQL_USERNAME}
spring.datasource.password=${SQL_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

logging.level.org.springframework.security=DEBUG

jwt.expiration.hours=24
```

Ensure the `SQL_USERNAME` and `SQL_PASSWORD` environment variables are set:

```bash
export SQL_USERNAME=devuser
export SQL_PASSWORD=devpassword
```

## Step 4: Build and Run the Application Locally

1. **Build the Project**:
    - Use Maven to build the project:
   ```bash
   ./mvnw clean install
   ```

2. **Run the Application**:
    - Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the Application**:
    - Open your web browser and navigate to `http://localhost:8080`.


## Troubleshooting

If you encounter any issues during installation or setup, consider the following:

- Ensure all services (MySQL) are running.
- Double-check your configuration files for typos or incorrect values.
- Check application logs for error messages and stack traces.

## Conclusion

By following these steps, you should have the User Authentication System up and running locally. This setup provides a robust foundation for developing and testing the application in different environments.

