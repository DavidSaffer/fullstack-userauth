# Installation Guide

This guide will walk you through the steps required to set up and run the User Authentication System, a Spring Boot application. The guide covers prerequisites and local setup.

## Prerequisites
Ensure the following tools are installed on your machine:
- **Docker**: [Get Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)
- **MySQL Server**: Ensure you have MySQL Server installed or have access to a MySQL instance. [Download MySQL](https://dev.mysql.com/downloads/mysql/)


## Step 1: Clone the Repository
Clone the project repository to your local machine:
```bash
git clone https://github.com/DavidSaffer/fullstack-userauth.git
cd fullstack-userauth
```

## Step 2: Configure the Database

1. **Start MySQL**:
    - Ensure your MySQL server is running. If not, start the MySQL service.

2. **Create Database**:
   - Execute the following SQL command to create a new database. This database will be used by the Spring Boot application to store user data and authentication details.
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

Ensure the `SQL_USERNAME` and `SQL_PASSWORD` environment variables are correctly set in the `dev.env` file, which should be located in the root of the `/backend` directory:
```plaintext
# dev.env
SQL_USERNAME=devuser
SQL_PASSWORD=devpassword
```

Open the `application.properties` file located in `src/main/resources` and configure the database connection details:

```properties
spring.datasource.url=jdbc:mysql://host.docker.internal:3306/user_auth
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

## Step 4: Start the Application with Docker Compose
From the root directory of the project, run:
```bash
docker-compose up --build
```
This command builds the images (if not already built) and starts the services defined in docker-compose.yml. It ensures that all services including the database are properly connected.

## Step 5: Access the Application

Once the containers are running, the application is accessible at:

- Frontend: http://localhost:3000 - The Next.js frontend
- Backend: http://localhost:8080 - The Spring Boot backend

## Step 6: Shutting Down
To stop and remove the containers, use the following command:
```bash
docker-compose down
```
## Troubleshooting

If you encounter issues with Docker or database connections:

- Ensure Docker and MySQL services are running.
- Check the Docker Compose logs for any connection errors.
- Ensure the database credentials and host are correctly set in your Docker environment settings.

## Conclusion

Following these steps, you should have the Full Stack User Authentication System running on your machine. This setup uses Docker for easy deployment and management, ensuring that your environment is consistent and manageable.

