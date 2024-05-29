# Frontend Installation Guide

This guide provides detailed instructions on how to set up and run the Next.js frontend for the Full Stack Application.

## Prerequisites

Before you begin, ensure you have the following software installed on your machine:

1. **Node.js**:
   - Version 14.x or later
   - [Download Node.js](https://nodejs.org/)

2. **npm (Node Package Manager)**:
   - Comes bundled with Node.js
   - Alternatively, you can use **yarn** if you prefer.

## Step 1: Navigate to the Frontend Directory
```bash
cd fullstack-userauth/frontend
```

## Step 2: Install Dependencies

Navigate to the `frontend` directory and install the project dependencies using npm or yarn:

### Using npm:
```bash
npm install
```

### Using yarn:
```bash
yarn install
```


## Step 3: Run the Development Server

Start the development server to run the Next.js application:

### Using npm:
```bash
npm run dev
```

### Using yarn:
```bash
yarn dev
```

This command starts the development server and makes your application available at `http://localhost:3000`.

## Step 5: Access the Application

Open your web browser and navigate to `http://localhost:3000`. You should see the Next.js application running.

## Additional Scripts

Here are some additional npm scripts that you can use for common tasks:

### Build the Application

Build the application for production:

```bash
npm run build
```

### Start the Application

Start the application in production mode:

```bash
npm run start
```

### Lint the Code

Run ESLint to find and fix problems in your code:

```bash
npm run lint
```

## Troubleshooting

If you encounter any issues during installation or setup, consider the following:

- Ensure all prerequisites are installed correctly.
- Check the terminal output for error messages and stack traces.
- Verify network connectivity and API endpoint availability.

## Conclusion

By following these steps, you should have the Next.js frontend up and running. This setup provides a robust foundation for developing and testing the frontend part of your full stack application.
