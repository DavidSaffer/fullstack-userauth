# Frontend for User Authentication System

## Overview

This frontend part of the Full Stack User Authentication System is built using Next.js. It provides a user-friendly interface for authentication and user management, interacting with a Spring Boot backend via REST APIs.

## Features

- **User Login and Registration**: Interfaces for users to register and log into the system.
- **Admin Interface**: Allows administrators to manage user roles and details.
- **Responsive Design**: Ensures the application is usable on both desktop and mobile devices.

## Tech Stack

- **Next.js**: A React framework for building user interfaces.
- **Axios**: Promise-based HTTP client for making API calls.

## Project Structure

- `app/`: Contains all the React components and pages.
  - `admin/`: Admin specific components.
  - `auth/`: Authentication related components.
- `interfaces/`: TypeScript interfaces used across the frontend application.
- `services/`: Services folder containing API calls setup.
- `public/`: Public assets like images and fonts.

## Getting Started

### Prerequisites

- Docker and Docker Compose: The application is containerized, which simplifies deployment and development workflows.

### Running the Application

1. **Clone the Repository**:
   If you haven't already, clone the main project repository to your local machine:

   ```bash
   git clone https://github.com/DavidSaffer/fullstack-userauth.git
   cd fullstack-userauth/frontend
   ```

2. **Build and Run with Docker**:
   From the root of the project, use Docker Compose to build and start the frontend service:

   ```bash
   docker-compose up --build frontend
   ```

   This command will build the Next.js application as a Docker container and run it. The service is defined in the root `docker-compose.yml`.

3. **Access the Application**:
   Once the application is running, you can access it at [http://localhost:3000](http://localhost:3000).

## Development

For local development, follow these steps:

1. **Install Dependencies**:
   Navigate to the `frontend` directory and install the necessary packages:

   ```bash
   npm install
   ```

2. **Run the Development Server**:
   Start the Next.js development server:

   ```bash
   npm run dev
   ```

   This will serve the application at [http://localhost:3000](http://localhost:3000).

## Troubleshooting

If you encounter any problems with running the frontend, please check the following:

- Ensure Docker is running properly.
- Check the Docker logs for any error messages.
- Ensure that the backend service is running, as the frontend requires it for full functionality.