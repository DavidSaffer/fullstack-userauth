# Installation Guide

This guide will help you set up the full stack application, which includes both the Spring Boot backend and the Next.js frontend. Follow the steps below to get started.

## Step 1: Clone the Repository

First, clone the project repository from GitHub to your local machine:

```bash
git clone https://github.com/DavidSaffer/fullstack-userauth.git
cd fullstack-userauth
```

## Step 2: Set Up the Backend

Navigate to the backend directory and follow the installation guide provided there to set up and run the Spring Boot backend:

[Backend Installation Guide](../backend/docs/INSTALLATION.md)

## Step 3: Set Up the Frontend

Navigate to the frontend directory and follow the installation guide provided there to set up and run the Next.js frontend:

[Frontend Installation Guide](../frontend/docs/INSTALLATION.md)

## Step 4: Running the Full Stack Application

Once both the backend and frontend are set up, you can run the full stack application. Typically, the backend will run on `http://localhost:8080` and the frontend on `http://localhost:3000`.

### Backend

Ensure your Spring Boot backend is running:

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

Ensure your Next.js frontend is running:

```bash
cd frontend
npm run dev
```

or if you are using yarn:

```bash
cd frontend
yarn dev
```

## Accessing the Application

Open your web browser and navigate to `http://localhost:3000`. You should be able to interact with the application, which will communicate with the backend running on `http://localhost:8080`.

## Conclusion

By following these steps, you will have the full stack application up and running. For more detailed information on each part of the application, refer to the respective documentation in the `backend/docs` and `frontend/docs` directories.