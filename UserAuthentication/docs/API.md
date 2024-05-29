# API Documentation 

## AuthController

### Overview
The `AuthController` manages all authentication-related operations such as user registration, login, logout, token validation, and user deletion within the application. It utilizes JSON Web Tokens (JWT) for maintaining the session state across requests.

---

### Endpoints

#### POST `/api/auth/signup`
Registers a new user with the system.

- **Request Body**: `SignupDTO` containing `username`, `password`, `email`, and `phone number`.
- **Responses**:
    - **200 OK**: Returns JWT token on successful registration. Includes token in a secure, HttpOnly cookie.
    - **400 Bad Request**: Registration failed, details provided in the response message.

#### POST `/api/auth/login`
Authenticates a user and issues a JWT token.

- **Request Body**: `LoginDTO` containing `username` and `password`.
- **Responses**:
    - **200 OK**: Login successful. Returns token in the response body and a secure, HttpOnly cookie.
    - **400 Bad Request**: Authentication failed, details provided in the response message.

#### POST `/api/auth/logout`
Logs out the current user by invalidating the JWT token cookie.

- **Responses**:
    - **200 OK**: Logged out successfully.

#### POST `/api/auth/validate-token`
Validates the provided JWT token for authenticity and expiration.

- **Request Body**: JWT token as a raw string.
- **Responses**:
    - **200 OK**: Token is valid.
    - **401 Unauthorized**: Token is invalid or expired.
    - **500 Internal Server Error**: Error occurred during token validation.

#### POST `/api/auth/is-admin`
Checks if the provided JWT token belongs to an admin user.

- **Request Body**: JWT token as a raw string.
- **Responses**:
    - **200 OK**: User is admin.
    - **403 Forbidden**: Access denied, user is not an admin.
    - **500 Internal Server Error**: Error occurred during role validation.

#### DELETE `/api/auth/user/{username}`
Deletes a user identified by `username`. Requires admin privileges or the user themselves requesting the deletion.

- **Path Variable**: `username` - the username of the user to delete.
- **Responses**:
    - **200 OK**: User deleted successfully. Additional data indicates if the logged-in user deleted their own account.
    - **400 Bad Request**: Deletion failed, details provided in the response message.
    - **403 Forbidden**: Access denied due to missing or invalid JWT token.
    - **500 Internal Server Error**: Error occurred during the deletion process.

---

### Authentication
All endpoints, except for `/signup` and `/login`, require a valid JWT token sent as an HttpOnly cookie named `jwt`. The token is validated for each request to ensure that it has not expired and corresponds to a valid user session.

---

### Error Handling
The API uses standard HTTP status codes to indicate the success or failure of an operation:
- `200 OK` for success.
- `400 Bad Request` for client-side input errors.
- `401 Unauthorized` for authentication errors.
- `403 Forbidden` for access violations.
- `500 Internal Server Error` for server-side issues.




## UserController

### Overview
The `UserController` handles user-specific operations such as retrieving, updating, and listing user information. It ensures that all interactions are secured and that user data can only be accessed or modified by authorized users, relying on JWT for authentication.

---

### Endpoints

#### GET `/api/user/get-user-info`
Retrieves the authenticated user's information based on the JWT token provided.

- **Required Cookies**: JWT token in an HttpOnly cookie named `jwt`.
- **Responses**:
    - **200 OK**: User information retrieved successfully. Returns `UserInfoDTO` containing `username`, `email`, `phone number`, and `role`.
    - **404 Not Found**: User not found.
    - **401 Unauthorized**: Invalid or expired JWT token.
    - **500 Internal Server Error**: Error occurred during information retrieval.

#### POST `/api/user/update-user`
Updates user details. Allows a user to update their own details or, if the user is an admin, the details of another user.

- **Request Body**: `EditUserDTO` containing `oldUsername`, `newUsername`, `email`, `phone number`, and `role`.
- **Required Cookies**: JWT token in an HttpOnly cookie named `jwt`.
- **Responses**:
    - **200 OK**: User updated successfully.
    - **400 Bad Request**: Error in user data update, details provided in the response message.
    - **401 Unauthorized**: Invalid or missing JWT token.
    - **500 Internal Server Error**: Error occurred during user update.

#### GET `/api/user/users`
Lists all users if the requester is an admin. This endpoint ensures that user listing is protected and only accessible by administrators.

- **Required Cookies**: JWT token in an HttpOnly cookie named `jwt`.
- **Responses**:
    - **200 OK**: Successfully retrieved list of all users.
    - **403 Forbidden**: Access denied, either because the JWT token does not belong to an admin or it is invalid.
    - **500 Internal Server Error**: Error occurred fetching user list.

---

### Authentication
Endpoints in the `UserController` require a valid JWT token sent as an HttpOnly cookie named `jwt`. This token is validated for each request to ensure it has not expired and belongs to a valid user session. Additionally, certain actions like updating user details or listing all users are restricted to users with specific roles (e.g., admin).

---

### Error Handling
The API uses standard HTTP status codes to indicate the success or failure of an operation:
- `200 OK` for successful operations.
- `400 Bad Request` for issues with the request's input data.
- `401 Unauthorized` for issues related to authentication.
- `403 Forbidden` for access violations.
- `500 Internal Server Error` for server-side issues affecting the operation.